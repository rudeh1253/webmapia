package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a user
 */
@Getter
@Setter
public class User {
    private final Long ID;
    private Character character;
    private List<SkillEffect> appliedSkills;
    private boolean isDead;
    private List<String> messagesAfterNight;

    public User(Long id) {
        this.ID = id;
        this.character = null;
        this.appliedSkills = new ArrayList<>();
        this.isDead = false;
        this.messagesAfterNight = new ArrayList<>();
    }

    /**
     * Use skill. The effect of skill is based on the character.
     * @param targetUser user instance of the target to apply skill.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    public SkillEffect activateSkill(User targetUser, SkillType skillType) {
        SkillEffect result = character.activateSkill(skillType);
        result.setActivator(this);
        result.setTarget(targetUser);
        return result;
    }

    /**
     * Apply skill which other user used for this user.
     * Several skills can be applied to this user.
     * If being dead, INVESTIGATE_ALIVE_CHARACTER won't be applied,
     * likewise, if being alive, INVESTIGATE_DEAD_CHARACTER won't be applied.
     * @param skillEffect information of skill to apply.
     */
    public synchronized void applySkill(SkillEffect skillEffect) {
        switch (skillEffect.getSkillType()) {
            case INVESTIGATE_ALIVE_CHARACTER:
                if (!isDead) {
                    appliedSkills.add(skillEffect);
                }
                break;
            case INVESTIGATE_DEAD_CHARACTER:
                if (isDead) {
                    appliedSkills.add(skillEffect);
                }
                break;
            default:
                appliedSkills.add(skillEffect);
        }
    }

    /**
     * Process applied skills on this character and determine the final effect after
     * the night.
     * @return list of effects as a result of the night with respect to this character.
     */
    public List<CharacterEffectAfterNight> resultOfNight() {
        List<CharacterEffectAfterNight> result = new ArrayList<>();
        processInvestigation(result);
        processKilling(result);
        return result;
    }

    private void processInvestigation(List<CharacterEffectAfterNight> result) {
        List<SkillEffect> investigations = appliedSkills.stream()
                .filter(se ->
                        se.getSkillType() == SkillType.INVESTIGATE_ALIVE_CHARACTER
                        || se.getSkillType() == SkillType.INVESTIGATE_DEAD_CHARACTER)
                .toList();
        investigations.forEach(inv -> {
                    if (inv.getSkillCondition().isSuccess(inv.getActivator(), inv.getTarget(), inv.getSkillType())) {
                        inv.getOnSkillSucceed().onSkillSucceed(inv.getActivator(), inv.getTarget(), inv.getSkillType());
                        result.add(new CharacterEffectAfterNight(
                                CharacterEffectAfterNightType.INVESTIGATE, inv.getActivator(), this
                        ));
                    }
                });
    }

    private void processKilling(List<CharacterEffectAfterNight> result) {
        CharacterEffectAfterNight killed = new CharacterEffectAfterNight();
        killed.setSkillTarget(this);
        killed.setType(CharacterEffectAfterNightType.NONE);

        Optional<SkillEffect> kill = appliedSkills.stream()
                .filter(skillEffect -> skillEffect.getSkillType() == SkillType.KILL)
                .findAny();
        Optional<SkillEffect> guard = appliedSkills.stream()
                .filter(skillEffect -> skillEffect.getSkillType() == SkillType.DEFENSE)
                .findAny();
        kill.ifPresent(e -> {
            if (e.getSkillCondition().isSuccess(e.getActivator(), e.getTarget(), e.getSkillType())) {
                killed.setType(CharacterEffectAfterNightType.KILL);
                killed.setSkillActivator(e.getActivator());
                guard.ifPresent(f -> {
                    killed.setType(CharacterEffectAfterNightType.FAIL_TO_KILL);
                    killed.setSkillActivator(f.getActivator());
                });
            }
        });

        Optional<SkillEffect> extermination = appliedSkills.stream()
                .filter(skillEffect -> skillEffect.getSkillType() == SkillType.EXTERMINATE)
                .findAny();
        extermination.ifPresent(e -> {
            killed.setType(CharacterEffectAfterNightType.KILL);
            killed.setSkillActivator(e.getActivator());
        });
        result.add(killed);
    }

    public void addMessageAfterNight(String message) {
        this.messagesAfterNight.add(message);
    }
}

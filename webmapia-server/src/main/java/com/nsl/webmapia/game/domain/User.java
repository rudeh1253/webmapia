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
@RequiredArgsConstructor
@Getter
@Setter
public class User {
    private final Long id;
    private Character character;
    private List<SkillEffect> appliedSkills;
    private boolean isDead;

    /**
     * Use skill. The effect of skill is based on the character.
     * @param targetUser user instance of the target to apply skill.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    public SkillEffect activateSkill(User targetUser, SkillType skillType) {
        return character.activateSkill(targetUser, skillType);
    }

    /**
     * Apply skill which other user used for this user.
     * Several skills can be applied to this user.
     * @param skillEffect information of skill to apply.
     */
    public synchronized void applySkill(SkillEffect skillEffect) {
        appliedSkills.add(skillEffect);
    }

    /**
     * Process applied skills on this character and determine the final effect after
     * the night.
     * @return list of effects as a result of the night with respect to this character.
     */
    public List<CharacterEffectAfterNight> resultOfNight() {
        List<CharacterEffectAfterNight> result = new ArrayList<>();
        List<SkillEffect> investigations = appliedSkills.stream()
                .filter(se ->
                        se.getSkillType() == SkillType.INVESTIGATE_ALIVE_CHARACTER
                        || se.getSkillType() == SkillType.INVESTIGATE_DEAD_CHARACTER)
                .toList();
        investigations.forEach(inv -> {
                    boolean isSuccess = character.applySkill(inv);
                    if (isSuccess) {
                        result.add(new CharacterEffectAfterNight(
                                CharacterEffectAfterNightType.INVESTIGATE, inv.getActivator(), this
                        ));
                    }
                });
        CharacterEffectAfterNight killed = new CharacterEffectAfterNight();
        killed.setSkillTarget(this);

        Optional<SkillEffect> kill = appliedSkills.stream()
                .filter(skillEffect -> skillEffect.getSkillType() == SkillType.KILL)
                .findAny();
        kill.ifPresent(e -> {
            killed.setType(CharacterEffectAfterNightType.KILL);
            killed.setSkillActivator(e.getActivator());
        });

        Optional<SkillEffect> guard = appliedSkills.stream()
                .filter(skillEffect -> skillEffect.getSkillType() == SkillType.DEFENSE)
                .findAny();
        guard.ifPresent(e -> {
            killed.setType(CharacterEffectAfterNightType.NONE);
            killed.setSkillActivator(e.getActivator());
        });

        Optional<SkillEffect> extermination = appliedSkills.stream()
                .filter(skillEffect -> skillEffect.getSkillType() == SkillType.EXTERMINATE)
                .findAny();
        extermination.ifPresent(e -> {
            killed.setType(CharacterEffectAfterNightType.KILL);
            killed.setSkillActivator(e.getActivator());
        });
        result.add(killed);
        return result;
    }
}

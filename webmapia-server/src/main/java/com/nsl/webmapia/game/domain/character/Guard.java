package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Guard implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.GUARD;
    private static final Faction FACTION = Faction.HUMAN;
    private SkillManager skillManager;

    @Autowired
    public Guard(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> {
            for (SkillEffect e : skillManager.getSkillEffects()) {
                if (e.getCharacterEffectAfterNightType() == CharacterEffectAfterNightType.KILL
                        && e.getSkillTargetUser().equals(tar)) {
                    return true;
                }
            }
            return false;
        });
        result.setOnSkillSucceed((src, tar, type) -> {
            skillManager.addSkillEffect(SkillEffect.builder()
                    .receiverUser(src)
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.GUARD)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .skillActivatorUser(src)
                    .skillTargetUser(tar)
                    .build());
            for (SkillEffect e : skillManager.getSkillEffects()) {
                if (e.getCharacterEffectAfterNightType() == CharacterEffectAfterNightType.KILL
                        && e.getSkillTargetUser().equals(tar)) {
                    e.setCharacterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_KILL);
                }
            }
        });
        result.setOnSkillFail((src, tar, type) -> skillManager.addSkillEffect(SkillEffect.builder()
                .receiverUser(src)
                .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_GUARD)
                .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                .skillActivatorUser(src)
                .skillTargetUser(tar)
                .build()));
        return result;
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CHARACTER_CODE;
    }

    @Override
    public Faction getFaction() {
        return FACTION;
    }
}

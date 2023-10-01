package com.nsl.webmapia.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.skill.domain.*;

public class Murderer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.MURDERER;
    private static final Faction FACTION = Faction.HUMAN;
    private boolean canKill = true;

    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
        if (skillType != SkillType.EXTERMINATE) {
            throw new CharacterNotSupportSkillTypeException("Murderer doesn't support given skill type: SkillType code"
                    + skillType);
        }
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> canKill && tar.getCharacter().getCharacterCode() != CharacterCode.HUMAN_MOUSE);
        result.setOnSkillSucceed((src, tar, type) -> {
            skillManager.addSkillEffect(SkillEffect.builder()
                    .receiverUser(null)
                    .skillActivatorUser(src)
                    .skillTargetUser(tar)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.EXTERMINATE)
                    .build());
            canKill = false;
            tar.setDead(true);
        });
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

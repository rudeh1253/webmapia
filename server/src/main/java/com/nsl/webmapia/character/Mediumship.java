package com.nsl.webmapia.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.skill.domain.*;

public class Mediumship implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.MEDIUMSHIP;
    private static final Faction FACTION = Faction.HUMAN;

    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
        if (skillType != SkillType.INVESTIGATE_DEAD_CHARACTER) {
            throw new CharacterNotSupportSkillTypeException("Mediumship doesn't support given skill type:"
                    + "SkillType code: " + skillType);
        }
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> tar.isDead());
        result.setOnSkillSucceed((src, tar, type) -> {
            SkillEffect notificationBody = SkillEffect.builder()
                    .skillTargetUser(tar)
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .receiverUser(src)
                    .skillActivatorUser(src)
                    .build();
            CharacterCode targetCharacterCode = tar.getCharacter().getCharacterCode();
            if (targetCharacterCode == CharacterCode.WOLF
            || targetCharacterCode == CharacterCode.GUARD
            || targetCharacterCode == CharacterCode.PREDICTOR) {
                notificationBody.setSkillTargetCharacterCode(targetCharacterCode);
            } else {
                notificationBody.setSkillTargetCharacterCode(CharacterCode.GOOD_MAN);
            }
            skillManager.addSkillEffect(notificationBody);
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

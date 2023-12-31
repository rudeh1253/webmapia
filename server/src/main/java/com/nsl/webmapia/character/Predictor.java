package com.nsl.webmapia.character;

import com.nsl.webmapia.skill.domain.*;

public class Predictor implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.PREDICTOR;
    private static final Faction FACTION = Faction.HUMAN;

    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> true);
        result.setOnSkillSucceed((src, tar, type) -> {
            SkillEffect skillEffect = SkillEffect.builder()
                    .skillTargetUser(tar)
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .skillActivatorUser(src)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .receiverUser(src)
                    .build();
            switch (tar.getCharacter().getCharacterCode()) {
                case MEDIUMSHIP:
                case GUARD:
                case WOLF:
                    skillEffect.setSkillTargetCharacterCode(tar.getCharacter().getCharacterCode());
                    break;
                case HUMAN_MOUSE:
                    skillEffect.setReceiverUser(null);
                    skillEffect.setCharacterEffectAfterNightType(CharacterEffectAfterNightType.KILL);
                    tar.setDead(true);
                    break;
                default:
                    skillEffect.setSkillTargetCharacterCode(CharacterCode.GOOD_MAN);
            }
            skillManager.addSkillEffect(skillEffect);
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

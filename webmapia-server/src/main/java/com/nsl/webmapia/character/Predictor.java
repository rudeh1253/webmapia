package com.nsl.webmapia.character;

import com.nsl.webmapia.skill.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.SkillEffect;
import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Predictor implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.PREDICTOR;
    private static final Faction FACTION = Faction.HUMAN;
    SkillManager skillManager;

    @Autowired
    public Predictor(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
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
                    break;
                case HUMAN_MOUSE:
                    skillEffect.setReceiverUser(null);
                    skillEffect.setCharacterEffectAfterNightType(CharacterEffectAfterNightType.KILL);
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

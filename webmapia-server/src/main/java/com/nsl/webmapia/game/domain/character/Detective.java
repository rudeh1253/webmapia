package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.skill.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Detective implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.DETECTIVE;
    private static final Faction FACTION = Faction.HUMAN;
    private SkillManager skillManager;

    @Autowired
    public Detective(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
        if (skillType != SkillType.INVESTIGATE_ALIVE_CHARACTER) {
            throw new CharacterNotSupportSkillTypeException("Detective doesn't support given skill type: SkillType code"
                    + skillType);
        }
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> tar.getCharacter().getCharacterCode() == CharacterCode.BETRAYER
                || tar.getCharacter().getCharacterCode() == CharacterCode.FOLLOWER);
        result.setOnSkillSucceed((src, tar, type) -> {
            SkillEffect skillEffect = SkillEffect.builder()
                    .receiverUser(src)
                    .skillTargetUser(tar)
                    .skillActivatorUser(src)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .build();
            skillManager.addSkillEffect(skillEffect);
        });
        result.setOnSkillFail((src, tar, type) -> {
            SkillEffect skillEffect = SkillEffect.builder()
                    .receiverUser(src)
                    .skillTargetUser(tar)
                    .skillActivatorUser(src)
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE)
                    .build();
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

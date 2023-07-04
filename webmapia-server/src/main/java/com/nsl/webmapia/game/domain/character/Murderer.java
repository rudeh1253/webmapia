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
public class Murderer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.MURDERER;
    private static final Faction FACTION = Faction.HUMAN;
    private boolean canKill = true;
    private SkillManager skillManager;

    @Autowired
    public Murderer(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
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

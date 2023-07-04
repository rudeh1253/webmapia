package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Soldier implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SOLDIER;
    private static final Faction FACTION = Faction.HUMAN;
    private int life = 1;
    private SkillManager skillManager;

    @Autowired
    public Soldier(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
        if (skillType != SkillType.GUARD) {
            throw new CharacterNotSupportSkillTypeException("Soldier doesn't support given skill type: SkillType code " + skillType);
        }
        ActivatedSkillInfo activatedSkillInfo = new ActivatedSkillInfo();
        activatedSkillInfo.setSkillCondition((src, tar, type) -> life > 0);
        activatedSkillInfo.setOnSkillSucceed((src, tar, type) -> {
            life--;
            skillManager.addSkillEffect(SkillEffect.builder()
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.GUARD)
                    .skillActivatorUser(src)
                    .skillTargetUser(tar)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .receiverUser(tar)
                    .build());
        });
        activatedSkillInfo.setSkillType(SkillType.GUARD);
        return activatedSkillInfo;
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

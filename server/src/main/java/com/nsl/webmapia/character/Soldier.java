package com.nsl.webmapia.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.skill.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.SkillEffect;
import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Soldier implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SOLDIER;
    private static final Faction FACTION = Faction.HUMAN;
    private int life = 1;

    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
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
            for (SkillEffect e : skillManager.getSkillEffects()) {
                if (e.getCharacterEffectAfterNightType() == CharacterEffectAfterNightType.KILL
                && e.getSkillTargetUser().equals(tar)) {
                    e.setCharacterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_KILL);
                }
            }
            tar.setDead(false);
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

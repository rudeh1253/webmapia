package com.nsl.webmapia.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.skill.domain.*;

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
        activatedSkillInfo.setSkillCondition((src, tar, type) -> {
            for (SkillEffect e : skillManager.getSkillEffects()) {
                if (e.getCharacterEffectAfterNightType() == CharacterEffectAfterNightType.KILL
                        && e.getSkillTargetUser().equals(tar)) {
                    return life > 0;
                }
            }
            return false;
        });
        activatedSkillInfo.setOnSkillSucceed((src, tar, type) -> {
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
            life--;
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

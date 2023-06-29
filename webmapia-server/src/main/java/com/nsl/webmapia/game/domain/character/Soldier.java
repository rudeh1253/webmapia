package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Soldier implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SOLDIER;
    private static final Faction FACTION = Faction.HUMAN;
    private int life = 2;

    @Value(value = "guardgettingattacked")
    private String attackedMsg;

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.DEFENSE) {
            throw new CharacterNotSupportSkillTypeException("Soldier doesn't support given skill type: SkillType code " + skillType);
        }
        SkillEffect skillEffect = new SkillEffect();
        skillEffect.setSkillCondition((src, tar, type) -> --life > 0);
        skillEffect.setSkillType(SkillType.DEFENSE);
        skillEffect.setResultMessageForTarget(Optional.of(attackedMsg));
        return skillEffect;
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

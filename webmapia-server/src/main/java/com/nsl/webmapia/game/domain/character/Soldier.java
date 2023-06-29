package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

public class Soldier implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SOLDIER;
    private static final Faction FACTION = Faction.HUMAN;
    private int life = 2;

    @Override
    public SkillEffect activateSkill(User activator, User targetUser, SkillType skillType) {
        if (skillType != SkillType.DEFENSE) {
            throw new CharacterNotSupportSkillTypeException("Solder doesn't support given skill type: SkillType code " + skillType);
        }
        // TODO: set skill condition
        SkillEffect skillEffect = new SkillEffect();
        skillEffect.setSkillType(SkillType.DEFENSE);
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

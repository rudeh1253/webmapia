package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

public class Wolf implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.WOLF;
    private static final Faction FACTION = Faction.WOLF;
    private int leftExtermination;

    public Wolf() {
        leftExtermination = 1;
    }

    @Override
    public SkillEffect activateSkill(User activator, User targetUser, SkillType skillType) {
        SkillEffect result = new SkillEffect();
        switch (skillType) {
            case EXTERMINATE:
                result.setSkillType(SkillType.EXTERMINATE);
                result.setSkillCondition((a, t, s) -> true);
                return result;
            case KILL:
                result.setSkillType(SkillType.KILL);
                return result;
            default:
                throw new CharacterNotSupportSkillTypeException("Wolf doesn't support given skill type: SkillType code " + skillType);
        }
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

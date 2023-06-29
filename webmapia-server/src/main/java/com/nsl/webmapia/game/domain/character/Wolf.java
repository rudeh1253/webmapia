package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;

public class Wolf implements Character {
    private static final int CHARACTER_CODE = 1;
    private static final Faction FACTION = Faction.WOLF;

    @Override
    public SkillInfo activateSkill(User targetUser, SkillType skillType) {
        SkillInfo result = new SkillInfo();
        switch (skillType) {
            case EXTERMINATE:
                result.setSkillType(SkillType.EXTERMINATE);
                return result;
            case KILL:
                result.setSkillType(SkillType.KILL);
                return result;
            default:
                throw new CharacterNotSupportSkillTypeException("Wolf doesn't support given skill type: " + skillType);
        }
    }
}

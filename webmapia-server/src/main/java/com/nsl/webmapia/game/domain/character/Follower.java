package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

public class Follower implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.FOLLOWER;
    private static final Faction FACTION = Faction.WOLF;

    @Override
    public SkillEffect activateSkill(User activator, User targetUser, SkillType skillType) {
        if (skillType != SkillType.INVESTIGATE_ALIVE_CHARACTER) {
            throw new CharacterNotSupportSkillTypeException("Follower doesn't support given skill type: SkillType code "
                    + skillType);
        }
        return null;
    }

    @Override
    public CharacterCode getCharacterCode() {
        return null;
    }

    @Override
    public Faction getFaction() {
        return null;
    }

    @Override
    public boolean applySkill(SkillEffect skillEffect) {
        return false;
    }
}

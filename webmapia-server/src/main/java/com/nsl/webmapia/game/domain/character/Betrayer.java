package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

public class Betrayer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.BETRAYER;
    private static final Faction FACTION = Faction.WOLF;

    @Override
    public SkillEffect activateSkill(User activator, User targetUser, SkillType skillType) {
        if (skillType != SkillType.INVESTIGATE_ALIVE_CHARACTER) {
            throw new CharacterNotSupportSkillTypeException("Betrayer doesn't support given skill type: SkillType code "
                    + skillType);
        }
        SkillEffect effect = new SkillEffect();
        effect.setSkillType(SkillType.INVESTIGATE_ALIVE_CHARACTER);
        effect.setTarget(targetUser);
        return effect;
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

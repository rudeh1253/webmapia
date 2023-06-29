package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Betrayer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.BETRAYER;
    private static final Faction FACTION = Faction.WOLF;

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.ENTER_WOLF_CHAT) {
            throw new CharacterNotSupportSkillTypeException("Betrayer doesn't support given skill type: SkillType code "
                    + skillType);
        }
        // TODO: set skill condition
        SkillEffect effect = new SkillEffect();
        effect.setSkillType(SkillType.ENTER_WOLF_CHAT);
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

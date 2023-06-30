package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

public class Successor implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SUCCESSOR;
    private static final Faction FACTION = Faction.HUMAN;

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect result = new SkillEffect();
        result.setSkillType(SkillType.NONE);
        result.setSkillCondition((src, tar, type) -> false);
        return result;
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

package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

public class Soldier implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SOLDIER;
    private static final Faction FACTION = Faction.HUMAN;
    private int life = 2;

    @Override
    public SkillEffect activateSkill(User targetUser, SkillType skillType) {
        SkillEffect skillEffect = new SkillEffect();
        skillEffect.setSkillType(SkillType.NONE);
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

    @Override
    public boolean applySkill(SkillEffect skillEffect) {
        switch (skillEffect.getSkillType()) {
            case KILL:
                life--;
                return life <= 0;
            default:
                return false;
        }
    }
}

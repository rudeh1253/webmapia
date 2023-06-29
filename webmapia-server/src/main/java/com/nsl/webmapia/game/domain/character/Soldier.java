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

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return this.life;
    }
}

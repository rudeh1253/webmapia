package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Soldier extends Character {
    private int life = 2;

    public Soldier(String memberId) {
        super(memberId);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.SOLDIER;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }

    @Override
    public boolean onKilled() {
        life--;
        if (life < 1) {
            super.dead = true;
        }
        return super.dead;
    }

    @Override
    public boolean onBeheaded() {
        life = 0;
        return super.onBeheaded();
    }
}

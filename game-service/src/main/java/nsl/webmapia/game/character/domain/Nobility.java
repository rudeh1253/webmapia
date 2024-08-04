package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Nobility extends Character {

    @Deprecated
    public Nobility(String memberId) {
        super(memberId);
    }

    public Nobility(String memberId, GameInstance gameInstance) {
        super(memberId, gameInstance);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.NOBILITY;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }

    @Override
    public boolean onExecuted() {
        return false;
    }
}

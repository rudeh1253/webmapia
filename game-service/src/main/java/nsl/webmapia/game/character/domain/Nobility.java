package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.gameoperation.domain.Vote;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Nobility extends Character {

    public Nobility(String memberId) {
        super(memberId);
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

    @Override
    public Vote vote(String targetId) {
        return new Vote(super.getMemberId(), targetId, 2);
    }
}

package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.gameoperation.domain.Vote;
import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Nobility extends Character {

    public Nobility(Member member) {
        super(member);
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
    public Vote vote(Member target) {
        return new Vote(super.getMember(), target, 2);
    }
}

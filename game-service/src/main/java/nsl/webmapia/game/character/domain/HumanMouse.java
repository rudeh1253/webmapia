package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class HumanMouse extends Character {

    public HumanMouse(Member member) {
        super(member);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.HUMAN_MOUSE;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN_MOUSE;
    }
}

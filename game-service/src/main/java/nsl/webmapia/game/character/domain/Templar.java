package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Templar extends Character {

    public Templar(Member member) {
        super(member);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return null;
    }

    @Override
    public Faction getFaction() {
        return null;
    }
}

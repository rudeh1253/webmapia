package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Citizen extends Character {

    public Citizen(Member member) {
        super(member);
    }

    /**
     * Citizen doesn't have any skill.
     * @param skillType type of skill to use, any type of skill is allowed.
     * @return skill activated by citizen doesn't have any effect.
     *         ActivatedSkillInfo.skillType: SkillType.NONE
     *         ActivatedSkillInfo.skillCondition: always false
     */
    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.CITIZEN;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}

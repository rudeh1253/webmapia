package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Betrayer extends Character {

    public Betrayer(Member member) {
        super(member);
    }

    /**
     * Activate one of skill of type, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER.
     * If skillType passed as a parameter is out of the two allowed SkillType, it will throw
     * CharacterNotSupportSkillTypeException as a RuntimeException.
     * @param skillType type of skill to use, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER
     *                  SkillType.ENTER_WOLF_CHAR: Check whether the target user is the wolf and if the target is the
     *                  wolf, enter the wolf chat.
     *                  SkillType.INVESTIGATE_DEAD_CHARACTER: Check the character of the dead user.
     * @return ActivatedSkillInfo Condition to succeed: the skill target has CharacterCode.WOLF.
     */
    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case ENTER_WOLF_CHAT -> new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                    tar.getCharacterCode() == CharacterCode.WOLF);
            case INVESTIGATE_DEAD_CHARACTER -> new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                    tar.isDead());
            default -> new SkillInfo();
        };
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.BETRAYER;
    }

    @Override
    public Faction getFaction() {
        return Faction.WOLF;
    }
}

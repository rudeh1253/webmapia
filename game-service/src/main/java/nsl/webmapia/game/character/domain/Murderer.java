package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Murderer extends Character {
    private int leftSkillCount = 1;

    public Murderer(String memberId) {
        super(memberId);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                leftSkillCount-- > 0 && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE);
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.MURDERER;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}

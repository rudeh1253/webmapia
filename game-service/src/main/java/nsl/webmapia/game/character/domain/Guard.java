package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.skill.domain.SkillCondition;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Guard extends Character {

    public Guard(String memberId) {
        super(memberId);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        SkillCondition condition = (act, tar, activatedSkillsToTarget) ->
                !activatedSkillsToTarget.contains(SkillType.BEHEAD)
                        && !activatedSkillsToTarget.contains(SkillType.MURDER)
                        && activatedSkillsToTarget.contains(SkillType.KILL);
        return new SkillInfo(skillType, condition);
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.GUARD;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}

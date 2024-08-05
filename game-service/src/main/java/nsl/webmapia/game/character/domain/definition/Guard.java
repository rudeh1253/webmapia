package nsl.webmapia.game.character.domain.definition;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.CharacterDefinition;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillCondition;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Guard implements CharacterDefinition {

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

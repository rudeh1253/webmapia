package nsl.webmapia.game.character.domain.definition;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.CharacterDefinition;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Murderer implements CharacterDefinition {
    private int leftSkillCount = 1;

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

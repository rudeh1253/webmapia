package nsl.webmapia.game.character.domain.definition;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.CharacterDefinition;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Templar implements CharacterDefinition {

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.TEMPLAR;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}

package nsl.webmapia.game.character.service.definition;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

@Component
public class NobilityCharacterDefinitionService implements CharacterDefinitionService {

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
}

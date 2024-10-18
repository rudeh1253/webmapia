package nsl.webmapia.game.domain.character.service.definition;

import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.character.domain.Faction;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionService;
import nsl.webmapia.game.domain.skill.domain.SkillInfo;
import nsl.webmapia.game.domain.skill.domain.SkillType;
import org.springframework.stereotype.Component;

@Component
public class SoldierCharacterDefinitionService implements CharacterDefinitionService {

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.SOLDIER;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}

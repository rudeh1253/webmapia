package nsl.webmapia.game.character.service.definition;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DetectiveCharacterDefinitionService implements CharacterDefinitionService {
    private static final Set<CharacterCode> SKILL_TARGET_CHARACTERS = Set.of(
            CharacterCode.BETRAYER,
            CharacterCode.FOLLOWER
    );

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                SKILL_TARGET_CHARACTERS.contains(tar.getCharacterCode()));
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.DETECTIVE;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}

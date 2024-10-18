package nsl.webmapia.game.character.service;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

import java.util.List;
import java.util.Map;

public interface CharacterDefinitionService {

    /**
     * Activate skill based on the character.
     *
     * @param skillType type of skill to use
     * @return information of activated skill.
     */
    SkillInfo getSkillOfType(SkillType skillType);

    /**
     * Return available skill types and for each skill type, available target ids.
     *
     * @param gameInstanceId        the GameInstance belongs to
     * @param characterAssignmentId of available skill types
     * @return a map which keys are available skill types and values are character assignment ids
     * of available targets for each skill type
     */
    default Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        return Map.of();
    }

    /**
     * Return code of the character.
     *
     * @return CharacterCode
     */
    CharacterCode getCharacterCode();

    /**
     * Return faction the character belongs to.
     *
     * @return Faction code from enum.
     */
    Faction getFaction();

    default int getVoteCount() {
        return 1;
    }
}

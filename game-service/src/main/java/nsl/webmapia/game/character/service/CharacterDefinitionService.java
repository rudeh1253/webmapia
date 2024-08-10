package nsl.webmapia.game.character.service;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

import java.util.List;

public interface CharacterDefinitionService {

    /**
     * Activate skill based on the character.
     *
     * @param skillType type of skill to use
     * @return information of activated skill.
     */
    SkillInfo getSkillOfType(SkillType skillType);

    default List<SkillType> getAvailableSkillTypes(int gameRoomId, String memberId) {
        return List.of();
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

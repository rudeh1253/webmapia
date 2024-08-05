package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public interface CharacterDefinition {

    /**
     * Activate skill based on the character.
     *
     * @param skillType type of skill to use
     * @return information of activated skill.
     */
    SkillInfo getSkillOfType(SkillType skillType);

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
}

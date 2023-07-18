package com.nsl.webmapia.character;

import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillType;

public interface Character {

    /**
     * Activate skill based on the character.
     * @param skillType type of skill to use
     * @return information of activated skill.
     */
    ActivatedSkillInfo activateSkill(SkillType skillType);

    /**
     * Return code of the character.
     * @return CharacterCode
     */
    CharacterCode getCharacterCode();

    /**
     * Return faction the character belongs to.
     * @return Faction code from enum.
     */
    Faction getFaction();
}

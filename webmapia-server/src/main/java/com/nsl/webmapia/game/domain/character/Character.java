package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;

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

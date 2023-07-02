package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;

public interface Character {

    /**
     * Use skill based on the character.
     * @param skillType type of skill
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
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

package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

public interface Character {

    /**
     * Use skill based on the character.
     * @Param activator user who invoked this method.
     * @param targetUser user instance of target of the skill. targetUser should be included
     *                   into the set of the participants.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    SkillEffect activateSkill(User activator, User targetUser, SkillType skillType);

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

    /**
     * After the end of the night, apply skill effects to the character.
     * @param skillEffect effect of the skill to apply.
     * @return true if the effect was successful, otherwise false.
     */
    boolean applySkill(SkillEffect skillEffect);
}

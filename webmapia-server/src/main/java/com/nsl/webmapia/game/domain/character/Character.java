package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillInfo;

public interface Character {

    /**
     * Use skill based on the character.
     * @param targetUser user instance of target of the skill. targetUser should be included
     *                   into the set of the participants.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    SkillInfo useSkill(User targetUser);
}

package com.nsl.webmapia.game.domain;

public interface Character {

    /**
     * Use skill based on the character.
     * @param targetUserId user id of target of the skill. targetUserId should be included
     *                     into the set of the participants.
     * @param skillType type of skill.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    SkillInfo skill(Long targetUserId, SkillType skillType);
}

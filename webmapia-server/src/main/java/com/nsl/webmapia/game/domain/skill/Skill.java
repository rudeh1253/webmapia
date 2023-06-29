package com.nsl.webmapia.game.domain.skill;

/**
 * Classes which implements this interface defines the behavior of skill.
 */
public interface Skill {

    /**
     * Use skill based on the character.
     * @param targetUserId user id of target of the skill. targetUserId should be included
     *                     into the set of the participants.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    SkillInfo activateSkill(Long targetUserId);
}

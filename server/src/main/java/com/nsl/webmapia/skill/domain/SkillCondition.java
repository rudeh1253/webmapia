package com.nsl.webmapia.skill.domain;

import com.nsl.webmapia.user.domain.User;

@FunctionalInterface
public interface SkillCondition {

    /**
     * Check condition of skill and based on the condition, determine whether
     * the skill is successful or not.
     * @param activator who used the skill
     * @param target of the skill
     * @param skillType type of the skill
     * @return true if successful, otherwise false.
     */
    boolean isSuccess(User activator, User target, SkillType skillType);
}

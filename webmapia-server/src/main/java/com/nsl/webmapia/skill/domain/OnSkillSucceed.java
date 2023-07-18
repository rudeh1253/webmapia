package com.nsl.webmapia.skill.domain;

import com.nsl.webmapia.user.domain.User;

@FunctionalInterface
public interface OnSkillSucceed {
    void onSkillSucceed(User src, User target, SkillType skillType);
}

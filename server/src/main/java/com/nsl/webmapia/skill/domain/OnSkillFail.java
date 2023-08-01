package com.nsl.webmapia.skill.domain;

import com.nsl.webmapia.user.domain.User;

@FunctionalInterface
public interface OnSkillFail {

    void onSkillFail(User src, User target, SkillType skillType);
}

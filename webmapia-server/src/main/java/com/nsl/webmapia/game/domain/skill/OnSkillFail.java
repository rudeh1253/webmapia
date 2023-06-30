package com.nsl.webmapia.game.domain.skill;

import com.nsl.webmapia.game.domain.User;

@FunctionalInterface
public interface OnSkillFail {

    void onSkillFail(User src, User target, SkillType skillType);
}

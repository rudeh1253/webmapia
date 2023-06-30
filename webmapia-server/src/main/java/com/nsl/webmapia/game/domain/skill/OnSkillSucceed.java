package com.nsl.webmapia.game.domain.skill;

import com.nsl.webmapia.game.domain.User;

@FunctionalInterface
public interface OnSkillSucceed {
    void onSkillSucceed(User src, User target, SkillType skillType);
}

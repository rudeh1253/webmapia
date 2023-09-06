package com.nsl.webmapia.skill.domain;

import com.nsl.webmapia.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ActivatedSkillInfo {
    private User activator;
    private User target;
    private SkillType skillType;
    private SkillCondition skillCondition;
    private OnSkillSucceed onSkillSucceed;
    private OnSkillFail onSkillFail;

    public ActivatedSkillInfo() {
        activator = null;
        target = null;
        skillType = null;
        skillCondition = (src, tar, type) -> false;
        onSkillSucceed = (src, tar, type) -> {};
        onSkillFail = (src, tar, type) -> {};
    }
}

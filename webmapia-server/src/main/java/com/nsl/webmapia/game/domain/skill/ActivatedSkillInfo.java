package com.nsl.webmapia.game.domain.skill;

import com.nsl.webmapia.game.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivatedSkillInfo {
    private User activator;
    private User target;
    private SkillType skillType;
    private SkillCondition skillCondition;
    private OnSkillSucceed onSkillSucceed;
    private OnSkillFail onSkillFail;
}

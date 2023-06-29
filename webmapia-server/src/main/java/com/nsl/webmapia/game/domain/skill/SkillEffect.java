package com.nsl.webmapia.game.domain.skill;

import com.nsl.webmapia.game.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillEffect {
    private User activator;
    private SkillType skillType;
}

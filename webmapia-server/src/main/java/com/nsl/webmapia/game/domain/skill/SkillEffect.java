package com.nsl.webmapia.game.domain.skill;

import com.nsl.webmapia.game.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class SkillEffect {
    private User activator;
    private User target;
    private SkillType skillType;
    private SkillCondition skillCondition;
    private Optional<String> resultMessageForSource = Optional.empty();
    private Optional<String> resultMessageForTarget = Optional.empty();
}

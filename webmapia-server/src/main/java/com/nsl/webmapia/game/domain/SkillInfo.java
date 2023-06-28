package com.nsl.webmapia.game.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillInfo {
    private Long targetUserId;
    private SkillType skillType;
}

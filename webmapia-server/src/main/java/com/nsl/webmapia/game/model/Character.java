package com.nsl.webmapia.game.model;

public interface Character {
    SkillInfo skill(Long targetUserId, SkillType skillType);
}

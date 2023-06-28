package com.nsl.webmapia.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private Long id;
    private Character character;

    public void useSkill(Long targetUserId, SkillType skillType) {
        SkillInfo result = character.skill(targetUserId, skillType);
        // TODO: logic after using skill
    }
}

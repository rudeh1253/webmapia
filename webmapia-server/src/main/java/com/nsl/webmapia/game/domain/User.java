package com.nsl.webmapia.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private Long id;
    private Character character;
    private boolean isDead;

    /**
     * Use skill. The effect of skill is based on the character.
     * @param targetUserId user id of the target of the skill.
     * @param skillType type of skill. targetUserId should be included
     *                  into the set of the participants.
     */
    public void useSkill(Long targetUserId, SkillType skillType) {
        SkillInfo result = character.skill(targetUserId, skillType);
        // TODO: logic after using skill
    }

    /**
     * This method is called when this user die.
     */
    public void die() {}

    /**
     * Check if this user has been dead.
     * @return true if the user has been dead, otherwise false.
     */
    public boolean dead() {
        return this.isDead;
    }
}

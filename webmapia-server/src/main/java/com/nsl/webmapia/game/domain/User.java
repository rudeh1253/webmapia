package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.skill.SkillInfo;
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
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    public SkillInfo useSkill(Long targetUserId) {
        return character.useSkill(targetUserId);
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

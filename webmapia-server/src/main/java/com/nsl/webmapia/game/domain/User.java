package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.skill.SkillInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user
 */
@RequiredArgsConstructor
@Getter
@Setter
public class User {
    private final Long id;
    private Character character;
    private List<SkillInfo> appliedSkills;
    private boolean isDead;

    /**
     * Use skill. The effect of skill is based on the character.
     * @param targetUser user instance of the target to apply skill.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    public SkillInfo useSkill(User targetUser) {
        return character.useSkill(targetUser);
    }

    /**
     * Apply skill which other user used for this user.
     * Several skills can be applied to this user.
     * @param skillInfo information of skill to apply.
     */
    public synchronized void applySkill(SkillInfo skillInfo) {
        appliedSkills.add(skillInfo);
    }
}

package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user
 */
@Getter
@Setter
public class User {
    private final Long ID;
    private Character character;
    private boolean isDead;
    private List<SkillNotificationBody> notificationAfterNight;

    public User(Long id) {
        this.ID = id;
        this.character = null;
        this.isDead = false;
        this.notificationAfterNight = new ArrayList<>();
    }

    /**
     * Use skill. The effect of skill is based on the character.
     * @param targetUser user instance of the target to apply skill.
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    public ActivatedSkillInfo activateSkill(User targetUser, SkillType skillType) {
        ActivatedSkillInfo result = character.activateSkill(skillType);
        result.setActivator(this);
        result.setTarget(targetUser);
        return result;
    }
}

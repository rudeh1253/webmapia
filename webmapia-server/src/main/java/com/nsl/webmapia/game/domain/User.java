package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
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
@ToString
public class User {
    private final Long ID;
    private Character character;
    private boolean isDead;

    public User(Long id) {
        this.ID = id;
        this.character = null;
        this.isDead = false;
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

    /**
     * Vote who is supposed to be executed.
     * @param subject who the user of this object determined to execute.
     * @return Vote object contains subject and voter of the vote.
     */
    public Vote vote(User subject) {
        return character.getCharacterCode() == CharacterCode.NOBILITY
                ? new Vote(2, this, subject)
                : new Vote(1, this, subject);
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User)obj).getID().equals(this.getID());
    }
}

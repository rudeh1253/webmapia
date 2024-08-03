package nsl.webmapia.game.character.domain;

import lombok.Getter;
import lombok.ToString;
import nsl.webmapia.game.gameoperation.domain.Vote;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

@Getter
@ToString
public abstract class Character {
    private final String memberId;

    protected boolean dead;

    private boolean disconnected;

    public Character(String memberId) {
        this.dead = false;
        this.memberId = memberId;
        this.disconnected = false;
    }

    /**
     * Activate skill based on the character.
     * @param skillType type of skill to use
     * @return information of activated skill.
     */
    public abstract SkillInfo getSkillOfType(SkillType skillType);

    /**
     * Return code of the character.
     * @return CharacterCode
     */
    public abstract CharacterCode getCharacterCode();

    /**
     * Return faction the character belongs to.
     * @return Faction code from enum.
     */
    public abstract Faction getFaction();

    /**
     * Called when this character get killed.
     * @return true if this character is supposed to be killed, otherwise false
     */
    public boolean onKilled() {
        this.dead = true;
        return true;
    }

    /**
     * Called when this character get executed by the result of the vote.
     * @return true if this character is supposed to be killed, otherwise false
     */
    public boolean onExecuted() {
        this.dead = true;
        return true;
    }

    /**
     * Called when this character get beheaded.
     * @return true if this character is supposed to be killed, otherwise false
     */
    public boolean onBeheaded() {
        this.dead = true;
        return true;
    }

    public Vote vote(String targetId) {
        return new Vote(this.memberId, targetId, 1);
    }
}

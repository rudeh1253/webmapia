package nsl.webmapia.game.domain.character.domain;

import lombok.Getter;
import lombok.ToString;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.skill.domain.SkillInfo;
import nsl.webmapia.game.domain.skill.domain.SkillType;

@Getter
@ToString
@Deprecated
public abstract class Character {
    private final String memberId;

    protected boolean dead;

    private boolean disconnected;
    private GameInstance gameInstance;

    @Deprecated
    public Character(String memberId) {
        this.dead = false;
        this.memberId = memberId;
        this.disconnected = false;
    }

    public Character(String memberId, GameInstance gameInstance) {
        this.dead = false;
        this.memberId = memberId;
        this.disconnected = false;
        this.gameInstance = gameInstance;
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
     * Called when this character getTitle killed.
     * @return true if this character is supposed to be killed, otherwise false
     */
    public boolean onKilled() {
        this.dead = true;
        return true;
    }

    /**
     * Called when this character getTitle executed by the result of the vote.
     * @return true if this character is supposed to be killed, otherwise false
     */
    public boolean onExecuted() {
        this.dead = true;
        return true;
    }

    /**
     * Called when this character getTitle beheaded.
     * @return true if this character is supposed to be killed, otherwise false
     */
    public boolean onBeheaded() {
        this.dead = true;
        return true;
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    public void onDisconnected() {
        this.disconnected = true;
    }
}

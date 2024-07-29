package nsl.webmapia.game.character.domain;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.exception.UnsupportedSkillTypeException;

import java.util.Map;
import java.util.Set;

import static nsl.webmapia.game.character.domain.CharacterCode.*;
import static nsl.webmapia.game.skill.domain.SkillType.*;

/**
 * A proxy object of Character object.
 * It provides additional function to check if the character supports the skill of type
 * given as a parameter of getSkillOfType method.
 * @author PGD
 */
public class CheckedCharacterProxy extends Character {
    private static final Map<CharacterCode, Set<SkillType>> SKILL_TYPE_SUPPORT_LIST = Map.ofEntries(
            Map.entry(WOLF, Set.of(KILL, BEHEAD)),
            Map.entry(BETRAYER, Set.of(ENTER_WOLF_CHAT, INVESTIGATE_DEAD_CHARACTER)),
            Map.entry(FOLLOWER, Set.of(ENTER_WOLF_CHAT, INVESTIGATE_ALIVE_CHARACTER)),
            Map.entry(PREDICTOR, Set.of(INVESTIGATE_ALIVE_CHARACTER)),
            Map.entry(MEDIUMSHIP, Set.of(INVESTIGATE_DEAD_CHARACTER)),
            Map.entry(DETECTIVE, Set.of(INVESTIGATE_ALIVE_CHARACTER)),
            Map.entry(SECRET_SOCIETY, Set.of()),
            Map.entry(NOBILITY, Set.of()),
            Map.entry(SOLDIER, Set.of()),
            Map.entry(TEMPLAR, Set.of()),
            Map.entry(CITIZEN, Set.of()),
            Map.entry(MURDERER, Set.of(KILL)),
            Map.entry(HUMAN_MOUSE, Set.of())
    );

    private final Character character;

    public CheckedCharacterProxy(Character character) {
        super(null);
        this.character = character;
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) throws UnsupportedSkillTypeException {
        if (isSkillNotSupported(skillType)) {
            throw new UnsupportedSkillTypeException(
                    String.format("%s doesn't support given skill type: %s",
                            this.character.getCharacterCode(),
                            skillType)
            );
        }
        return this.character.getSkillOfType(skillType);
    }

    private boolean isSkillNotSupported(SkillType skillType) {
        return !SKILL_TYPE_SUPPORT_LIST.get(this.character.getCharacterCode()).contains(skillType);
    }

    @Override
    public CharacterCode getCharacterCode() {
        return this.character.getCharacterCode();
    }

    @Override
    public Faction getFaction() {
        return this.character.getFaction();
    }
}

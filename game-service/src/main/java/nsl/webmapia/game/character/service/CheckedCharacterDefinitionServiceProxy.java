package nsl.webmapia.game.character.service;

import nsl.webmapia.game.character.domain.Character;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.exception.UnsupportedSkillTypeException;

import java.util.Map;
import java.util.Set;

import static nsl.webmapia.game.character.domain.CharacterCode.*;
import static nsl.webmapia.game.skill.domain.SkillType.*;

/**
 * A proxy object for CharacterDefinitionService implementation.
 * It provides additional function to check if the character supports the skill of type
 * given as a parameter of getSkillOfType method.
 * @author PGD
 */
public class CheckedCharacterDefinitionServiceProxy implements CharacterDefinitionService {
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

    private final CharacterDefinitionService proxyFor;

    public CheckedCharacterDefinitionServiceProxy(CharacterDefinitionService proxyFor) {
        this.proxyFor = proxyFor;
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) throws UnsupportedSkillTypeException {
        if (isSkillNotSupported(skillType)) {
            throw new UnsupportedSkillTypeException(
                    String.format("%s doesn't support given skill type: %s",
                            this.proxyFor.getCharacterCode(),
                            skillType)
            );
        }
        return this.proxyFor.getSkillOfType(skillType);
    }

    private boolean isSkillNotSupported(SkillType skillType) {
        return !SKILL_TYPE_SUPPORT_LIST.get(this.proxyFor.getCharacterCode()).contains(skillType);
    }

    @Override
    public CharacterCode getCharacterCode() {
        return this.proxyFor.getCharacterCode();
    }

    @Override
    public Faction getFaction() {
        return this.proxyFor.getFaction();
    }
}

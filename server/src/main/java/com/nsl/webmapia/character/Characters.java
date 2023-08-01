package com.nsl.webmapia.character;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Component
public class Characters {
    private final Map<CharacterCode, Character> characters;

    public Characters(Wolf wolf,
                      Betrayer betrayer,
                      Citizen citizen,
                      Detective detective,
                      Follower follower,
                      Guard guard,
                      HumanMouse humanMouse,
                      Mediumship mediumship,
                      Murderer murderer,
                      Nobility nobility,
                      Predictor predictor,
                      SecretSociety secretSociety,
                      Soldier soldier,
                      Successor successor,
                      Templar templar) {
        characters = Map.ofEntries(
                Map.entry(CharacterCode.WOLF, wolf),
                Map.entry(CharacterCode.BETRAYER, betrayer),
                Map.entry(CharacterCode.DETECTIVE, detective),
                Map.entry(CharacterCode.FOLLOWER, follower),
                Map.entry(CharacterCode.CITIZEN, citizen),
                Map.entry(CharacterCode.GUARD, guard),
                Map.entry(CharacterCode.HUMAN_MOUSE, humanMouse),
                Map.entry(CharacterCode.MEDIUMSHIP, mediumship),
                Map.entry(CharacterCode.MURDERER, murderer),
                Map.entry(CharacterCode.NOBILITY, nobility),
                Map.entry(CharacterCode.PREDICTOR, predictor),
                Map.entry(CharacterCode.SECRET_SOCIETY, secretSociety),
                Map.entry(CharacterCode.SOLDIER, soldier),
                Map.entry(CharacterCode.SUCCESSOR, successor),
                Map.entry(CharacterCode.TEMPLAR, templar)
        );
    }
}

import {CharacterDistribution} from "../type/gameDomainType";

export function sumCharacterDistribution(charDist: CharacterDistribution) {
    let sum = 0;
    const characters = [
        "BETRAYER",
        "CITIZEN",
        "DETECTIVE",
        "FOLLOWER",
        "GUARD",
        "HUMAN_MOUSE",
        "MEDIUMSHIP",
        "MURDERER",
        "NOBILITY",
        "PREDICTOR",
        "SECRET_SOCIETY",
        "SOLDIER",
        "SUCCESSOR",
        "TEMPLAR",
        "WOLF"
    ];
    for (let key of characters) {
        const k = key as
            | "BETRAYER"
            | "CITIZEN"
            | "DETECTIVE"
            | "FOLLOWER"
            | "GUARD"
            | "HUMAN_MOUSE"
            | "MEDIUMSHIP"
            | "MURDERER"
            | "NOBILITY"
            | "PREDICTOR"
            | "SECRET_SOCIETY"
            | "SOLDIER"
            | "SUCCESSOR"
            | "TEMPLAR"
            | "WOLF";
        sum += charDist[k];
    }
    return sum;
}

export type CharacterGenerationRequest = {
    notificationType: string;
    gameId: number;
    characterDistribution: {
        "WOLF": number;
        "BETRAYER": number;
        "FOLLOWER": number;
        "PREDICTOR": number;
        "GUARD": number;
        "MEDIUMSHIP": number;
        "DETECTIVE": number;
        "SUCCESSOR": number;
        "SECRET_SOCIETY": number;
        "NOBILITY": number;
        "SOLDIER": number;
        "TEMPLAR": number;
        "CITIZEN": number;
        "MURDERER": number;
        "HUMAN_MOUSE": number;
    }
};

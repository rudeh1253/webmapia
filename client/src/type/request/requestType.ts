export type CharacterGenerationRequest = {
    notificationType: string;
    gameId: number;
    characterDistribution: {
        WOLF: number;
        BETRAYER: number;
        FOLLOWER: number;
        PREDICTOR: number;
        GUARD: number;
        MEDIUMSHIP: number;
        DETECTIVE: number;
        SUCCESSOR: number;
        SECRET_SOCIETY: number;
        NOBILITY: number;
        SOLDIER: number;
        TEMPLAR: number;
        CITIZEN: number;
        MURDERER: number;
        HUMAN_MOUSE: number;
    };
};

export type GameStartNotificationRequest = {
    notificationType: string;
    gameSetting: string;
    gameId: number;
};

export type PhaseEndRequest = {
    notificationType: string;
    gameId: number;
    userId: number;
};

export type PostPhaseRequest = {
    notificationType: string;
    gameId: number;
};

export type VoteRequest = {
    notificationType: string;
    gameId: number;
    voterId: number;
    subjectId: number;
};

export type RoomCreationRequest = {
    gameName: string;
    hostId: number;
    hostName: string;
};

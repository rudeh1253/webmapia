export type CharacterGenerationResponse = {
    notificationType: string;
    receiverId: number;
    characterCode: string;
    gameId: number;
};

export type CurrentPhaseResponse = {
    gameId: number;
    gamePhase: string;
};

export type PhaseEndNotificationResponse = {
    notificationType: string;
    gameId: number;
    isEnd: boolean;
};

export type PhaseResultResponse = {
    notificationType: string;
    gameId: number;
    isGameEnd: boolean;
    winner: string;
};

export type VoteResultResponse = {
    notificationType: string;
    gameId: number;
    idOfUserToBeExecuted: number;
};

export type RoomInfoResponse = {
    notificationType: string;
    roomId: number;
    hostId: number;
    roomName: string;
    users: UserResponse[];
};

export type UserResponse = {
    notificationType: string;
    gameId: number;
    userId: number;
    username: string;
    characterCode: string;
    isDead: boolean;
};

export type SkillResultResponse = {
    notificationType: string;
    gameId: number;
    skillTargetId: number;
    skillActivatorId: number;
    receiverId: number;
    message: string;
    characterEffectAfterNightType: string;
};

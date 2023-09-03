export type RoomInfo = {
    roomId: number;
    hostId: number;
    roomName: string;
    numOfUsers: number;
};

export type UserInfo = {
    userId: number;
    username: string;
    characterCode: CharacterCode;
    isDead: boolean;
};

export type PublicChatMessage = {
    gameId: number;
    senderId: number;
    message: string;
};

export type PrivateChatMessage = PublicChatMessage & {
    receiverUserIds: number[];
};

export type CharacterCode =
    | "WOLF"
    | "BETRAYER"
    | "FOLLOWER"
    | "PREDICTOR"
    | "GUARD"
    | "MEDIUMSHIP"
    | "DETECTIVE"
    | "CHASER"
    | "SUCCESSOR"
    | "SECRET_SOCIETY"
    | "NOBILITY"
    | "SOLDIER"
    | "TEMPLAR"
    | "CITIZEN"
    | "MURDERER"
    | "HUMAN_MOUSE"
    | "GOOD_MAN"
    | null;

export type Chat = {
    senderId: number;
    message: string;
    timestamp: number;
    isPublic: boolean;
    isMe: boolean;
};

export type GameSetting = {
    discussionTimeSeconds: number;
    voteTimeSeconds: number;
    nightTimeSeconds: number;
};

export enum GamePhase {
    NIGHT = "NIGHT",
    DAYTIME = "DAYTIME",
    VOTE = "VOTE",
    EXECUTION = "EXECUTION",
    CHARACTER_DISTRIBUTION = "CHARACTER_DISTRIBUTION"
}

export type CharacterDistribution = {
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

export type ChatStorage = {
    id: number;
    participants: UserInfo[];
    name: string;
    chatLogs: Chat[];
};

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
    containerId: number;
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
    CHARACTER_DISTRIBUTION = "CHARACTER_DISTRIBUTION",
    GAME_END = "GAME_END"
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

export type ChatContainer = {
    id: number;
    participants: number[];
    name: string;
    chatLogs: Chat[];
};

export type Chat = {
    senderId: number;
    message: string;
    timestamp: number;
    containerId: number;
    isMe: boolean;
};

export type ChatContainerTab = {
    key: number;
    name: string;
};

export type CharacterEffectAfterNightType =
    | "KILL"
    | "INVESTIGATE"
    | "GUARD"
    | "EXTERMINATE"
    | "FAIL_TO_KILL"
    | "FAIL_TO_INVESTIGATE"
    | "FAIL_TO_GUARD"
    | "FAIL_TO_EXTERMINATE"
    | "ENTER_WOLF_CHAT"
    | "NOTIFY"
    | "NONE";

export type SkillType =
    | "KILL"
    | "EXTERMINATE"
    | "INVESTIGATE_DEAD_CHARACTER"
    | "INVESTIGATE_ALIVE_CHARACTER"
    | "GUARD"
    | "ENTER_WOLF_CHAT"
    | "NONE";

export type Faction = "WOLF" | "HUMAN" | "HUMAN_MOUSE" | null;

export type PhaseResult = {
    winner: Faction;
    users: UserInfo[];
};

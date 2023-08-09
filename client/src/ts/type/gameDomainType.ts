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
    sender: UserInfo;
    message: string;
    timestamp: number;
    isPublic: boolean;
    isMe: boolean;
};

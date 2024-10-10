import {
    CharacterCode,
    CharacterEffectAfterNightType,
    Faction,
    GamePhase,
    GameSetting
} from "./gameDomainType";
import {NotificationType} from "./notificationType";

export interface CommonResponse<D> {
    statusCode: number;
    statusCodeSeries: number;
    message: string;
    content: D
}

export type CharacterGenerationResponse = {
    notificationType: NotificationType;
    receiverId: number;
    characterCode: CharacterCode;
    gameId: number;
};

export type CurrentPhaseResponse = {
    gameId: number;
    gamePhase: string;
};

export type PhaseEndNotificationResponse = {
    notificationType: NotificationType;
    gameId: number;
    isEnd: boolean;
};

export type PhaseResultResponse = {
    notificationType: NotificationType;
    gameId: number;
    targetUserId: number;
    gameEnd: boolean;
    winner: Faction;
    endedPhase: GamePhase;
    skillResults: SkillResultResponse[];
    voteResult: VoteResultResponse;
};

export type VoteResultResponse = {
    notificationType: NotificationType;
    gameId: number;
    idOfUserToBeExecuted: number;
};

export type RoomListResponse = {
    page: number;
    totalPage: number;
    totalElementCount: number;
    elements: {
        roomId: number;
        roomName: string;
        hostMemberId: string;
        creationTime: Date;
        participantsIds: string[];
    }[]
};

export type RoomCreationResponse = {
    roomId: number;
    roomName: string;
    hostMemberId: string;
    creationTime: Date;
}

export type UserResponse = {
    notificationType: NotificationType;
    gameId: number;
    userId: number;
    username: string;
    characterCode: CharacterCode;
    isDead: boolean;
};

export type SkillResultResponse = {
    notificationType: NotificationType;
    gameId: number;
    skillTargetId: number;
    skillActivatorId: number;
    receiverId: number;
    message: string;
    skillTargetCharacterCode: CharacterCode;
    skillTargetUsername: string;
    characterEffectAfterNightType: CharacterEffectAfterNightType;
};

export type GameStartNotificationResponse = {
    notificationType: NotificationType;
    gameSetting: GameSetting;
    gameId: number;
};

export type ParticipateChatContainerResponse = {
    notificationType: NotificationType;
    gameId: number;
    newParticipant: number;
    previousParticipants: number[];
    containerId: number;
    containerName: string;
};

export type NewParticipantResponse = {
    notificationType: NotificationType;
    gameId: number;
    containerId: number;
    containerName: string;
    newUserId: number;
    userIdsParticipatingAlready: number[];
};

export type RemoveChatContainerResponse = {
    notificationType: NotificationType;
    gameId: number;
    containerId: number;
    participants: number[];
};

export type RoomAvailabilityResponse = {
    available: boolean;
};

export type GameEndResponse = {
    notificationType: NotificationType;
    gameId: number;
};

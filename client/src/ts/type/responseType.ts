import { CharacterCode, GameSetting } from "./gameDomainType";
import {NotificationType} from "./notificationType";

export interface CommonResponse<D> {
    status: number;
    message: string;
    dateTime: string;
    data: D;
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
    winner: string;
};

export type VoteResultResponse = {
    notificationType: NotificationType;
    gameId: number;
    idOfUserToBeExecuted: number;
};

export type RoomInfoResponse = {
    notificationType: NotificationType;
    roomId: number;
    hostId: number;
    roomName: string;
    users: UserResponse[];
};

export type UserResponse = {
    notificationType: NotificationType;
    gameId: number;
    userId: number;
    username: string;
    characterCode: string;
    isDead: boolean;
};

export type SkillResultResponse = {
    notificationType: NotificationType;
    gameId: number;
    skillTargetId: number;
    skillActivatorId: number;
    receiverId: number;
    message: string;
    characterEffectAfterNightType: string;
};

export type GameStartNotificationResponse = {
    notificationType: NotificationType;
    gameSetting: GameSetting;
    gameId: number;
};

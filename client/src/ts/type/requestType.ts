import {CharacterDistribution, GameSetting} from "./gameDomainType";
import {NotificationType} from "./notificationType";

export type CharacterGenerationRequest = {
    notificationType: NotificationType;
    gameId: number;
    characterDistribution: CharacterDistribution;
};

export type GameStartNotificationRequest = {
    notificationType: NotificationType;
    gameSetting: GameSetting;
    gameId: number;
};

export type PhaseEndRequest = {
    notificationType: NotificationType;
    gameId: number;
    userId: number;
};

export type PostPhaseRequest = {
    notificationType: NotificationType;
    gameId: number;
    userId: number;
};

export type VoteRequest = {
    notificationType: NotificationType;
    gameId: number;
    voterId: number;
    subjectId: number;
};

export type RoomCreationRequest = {
    gameName: string;
    hostId: number;
    hostName: string;
};

export type UserRequest = {
    notificationType: NotificationType;
    gameId: number;
    userId: number;
    username: string;
};

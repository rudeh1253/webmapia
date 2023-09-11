import {
    CharacterDistribution,
    GamePhase,
    GameSetting,
    SkillType
} from "./gameDomainType";

export type CharacterGenerationRequest = {
    gameId: number;
    characterDistribution: CharacterDistribution;
};

export type GameStartRequest = {
    gameSetting: GameSetting;
    gameId: number;
};

export type PhaseEndRequest = {
    gameId: number;
    userId: number;
    gamePhase: GamePhase;
};

export type PostPhaseRequest = {
    gameId: number;
    userId: number;
};

export type VoteRequest = {
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
    gameId: number;
    userId: number;
    username: string;
};

export type ParticipateChatContainerRequest = {
    gameId: number;
    containerId: number
    containerName: string;
    participant: number;
};

export type NewParticipantRequest = {
    gameId: number;
    containerId: number;
    userId: number;
};

export type RemoveChatContainerRequest = {
    gameId: number;
    containerId: number;
};

export type SkillActivationRequest = {
    gameId: number;
    activatorId: number;
    targetId: number;
    skillType: SkillType;
};

export type ProcessSkillsRequest = {
    gameId: number;
};

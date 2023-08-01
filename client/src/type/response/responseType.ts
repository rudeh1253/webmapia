export type CharacterGenerationResponse = {
    notificationType: string;
    receiverId: number;
    characterCode: string;
    gameId: number;
}

export type CurrentPhaseResponse = {
    gameId: number;
    gamePhase: string;
}
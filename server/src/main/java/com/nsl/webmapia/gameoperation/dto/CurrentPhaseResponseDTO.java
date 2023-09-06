package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.gameoperation.domain.GamePhase;
import lombok.*;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class CurrentPhaseResponseDTO {
    private Long gameId;
    private GamePhase gamePhase;

    public static CurrentPhaseResponseDTO of(Long gameId, GamePhase gamePhase) {
        return CurrentPhaseResponseDTO.builder()
                .gameId(gameId)
                .gamePhase(gamePhase)
                .build();
    }
}

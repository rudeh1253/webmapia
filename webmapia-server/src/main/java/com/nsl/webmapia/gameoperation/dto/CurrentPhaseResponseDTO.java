package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.gameoperation.domain.GamePhase;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
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

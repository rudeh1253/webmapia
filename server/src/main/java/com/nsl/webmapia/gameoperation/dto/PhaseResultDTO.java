package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.character.Faction;
import com.nsl.webmapia.common.NotificationType;
import lombok.*;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class PhaseResultDTO {
    private NotificationType notificationType;
    private Long gameId;
    private boolean isGameEnd;
    private Faction winner;

    public static PhaseResultDTO of(Long gameId, boolean isGameEnd, Faction winner) {
        return PhaseResultDTO.builder()
                .notificationType(NotificationType.PHASE_RESULT)
                .gameId(gameId)
                .isGameEnd(isGameEnd)
                .winner(winner)
                .build();
    }
}

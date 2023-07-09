package com.nsl.webmapia.game.dto;

import com.nsl.webmapia.game.domain.notification.GameNotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
@ToString
public class VoteResultResponseDTO {
    private final GameNotificationType notificationType;
    private final Long gameId;
    private final Long idOfUserToBeExecuted;

    private VoteResultResponseDTO(GameNotificationType notificationType, Long gameId, Long idOfUserToBeExecuted) {
        this.notificationType = notificationType;
        this.gameId = gameId;
        this.idOfUserToBeExecuted = idOfUserToBeExecuted;
    }

    public static VoteResultResponseDTO from(GameNotificationType notificationType, Long gameId, Long idOfUserToBeExecuted) {
        return VoteResultResponseDTO.builder()
                .notificationType(notificationType)
                .gameId(gameId)
                .idOfUserToBeExecuted(idOfUserToBeExecuted)
                .build();
    }
}

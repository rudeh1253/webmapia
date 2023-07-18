package com.nsl.webmapia.gameoperation.dto.response;

import com.nsl.webmapia.common.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
@ToString
public class VoteResultResponseDTO {
    private final NotificationType notificationType;
    private final Long gameId;
    private final Long idOfUserToBeExecuted;

    private VoteResultResponseDTO(NotificationType notificationType, Long gameId, Long idOfUserToBeExecuted) {
        this.notificationType = notificationType;
        this.gameId = gameId;
        this.idOfUserToBeExecuted = idOfUserToBeExecuted;
    }

    public static VoteResultResponseDTO from(NotificationType notificationType, Long gameId, Long idOfUserToBeExecuted) {
        return VoteResultResponseDTO.builder()
                .notificationType(notificationType)
                .gameId(gameId)
                .idOfUserToBeExecuted(idOfUserToBeExecuted)
                .build();
    }
}

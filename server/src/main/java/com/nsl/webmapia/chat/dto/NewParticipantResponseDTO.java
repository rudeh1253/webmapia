package com.nsl.webmapia.chat.dto;

import com.nsl.webmapia.common.NotificationType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class NewParticipantResponseDTO {
    private NotificationType notificationType;
    private Long gameId;
    private Long containerId;
    private Long userId;
    private List<Long> receiverIds;

    public static NewParticipantResponseDTO from(Long gameId, Long containerId, Long userId, List<Long> receiverIds) {
        return NewParticipantResponseDTO.builder()
                .notificationType(NotificationType.NEW_PARTICIPANT_IN_CHAT_CONTAINER)
                .gameId(gameId)
                .containerId(containerId)
                .userId(userId)
                .receiverIds(receiverIds)
                .build();
    }
}

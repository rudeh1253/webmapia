package com.nsl.webmapia.chat.dto;

import com.nsl.webmapia.chat.domain.ChatContainer;
import com.nsl.webmapia.common.NotificationType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class RemoveChatContainerResponseDTO {
    private NotificationType notificationType;
    private Long gameId;
    private Long containerId;
    private List<Long> participants;

    public static RemoveChatContainerResponseDTO from(ChatContainer chatContainer) {
        return RemoveChatContainerResponseDTO.builder()
                .notificationType(NotificationType.REMOVE_CHAT_CONTAINER)
                .gameId(chatContainer.getGameId())
                .containerId(chatContainer.getContainerId())
                .participants(chatContainer.getParticipantIds())
                .build();
    }
}

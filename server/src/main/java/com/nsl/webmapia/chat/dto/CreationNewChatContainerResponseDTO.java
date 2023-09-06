package com.nsl.webmapia.chat.dto;

import com.nsl.webmapia.chat.domain.ChatContainer;
import com.nsl.webmapia.common.NotificationType;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class CreationNewChatContainerResponseDTO {
    private NotificationType notificationType;
    private Long gameId;
    private List<Long> participants;
    private Long containerId;
    private String containerName;

    public static CreationNewChatContainerResponseDTO from(ChatContainer chatContainer) {
        return CreationNewChatContainerResponseDTO.builder()
                .notificationType(NotificationType.CREATE_NEW_CHAT_CONTAINER)
                .gameId(chatContainer.getGameId())
                .participants(chatContainer.getParticipantIds())
                .containerId(chatContainer.getContainerId())
                .containerName(chatContainer.getContainerName())
                .build();
    }
}

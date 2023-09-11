package com.nsl.webmapia.chat.dto;

import com.nsl.webmapia.chat.domain.ChatContainer;
import com.nsl.webmapia.common.NotificationType;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParticipateChatContainerResponseDTO {
    private NotificationType notificationType;
    private Long gameId;
    private Long newParticipant;
    private List<Long> previousParticipants;
    private Long containerId;
    private String containerName;

    public static ParticipateChatContainerResponseDTO from(ChatContainer chatContainer, List<Long> previousParticipants, Long newParticipant) {
        return ParticipateChatContainerResponseDTO.builder()
                .notificationType(NotificationType.PARTICIPATE_CHAT_CONTAINER)
                .gameId(chatContainer.getGameId())
                .newParticipant(newParticipant)
                .previousParticipants(previousParticipants)
                .containerId(chatContainer.getContainerId())
                .containerName(chatContainer.getContainerName())
                .build();
    }
}

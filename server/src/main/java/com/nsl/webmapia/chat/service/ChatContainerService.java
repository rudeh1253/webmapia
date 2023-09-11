package com.nsl.webmapia.chat.service;

import com.nsl.webmapia.chat.domain.ChatContainer;
import com.nsl.webmapia.chat.dto.ParticipateChatContainerResponseDTO;
import com.nsl.webmapia.chat.dto.ParticipateChatContainerRequestDTO;
import com.nsl.webmapia.chat.dto.RemoveChatContainerResponseDTO;
import com.nsl.webmapia.chat.repository.ChatContainerRepository;
import com.nsl.webmapia.common.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatContainerService {
    private ChatContainerRepository containerRepository;

    @Autowired
    public ChatContainerService(ChatContainerRepository containerRepository) {
        this.containerRepository = containerRepository;
    }

    public synchronized ParticipateChatContainerResponseDTO participateChatContainer(ParticipateChatContainerRequestDTO requestDTO) {
        Long gameId = requestDTO.getGameId();
        Long containerId = requestDTO.getContainerId();
        String containerName = requestDTO.getContainerName();
        Long participant = requestDTO.getParticipant();

        Optional<ChatContainer> chatContainerWrapper =
                containerRepository.findByGameIdAndContainerId(gameId, containerId);

        ChatContainer chatContainer = chatContainerWrapper.orElseGet(() -> {
            ChatContainer newChatcontainer = new ChatContainer(gameId, containerId, containerName);
            containerRepository.save(newChatcontainer);
            return newChatcontainer;
        });
        List<Long> previousParticipantIds = chatContainer.getParticipantIds();
        chatContainer.addParticipant(participant);
        return ParticipateChatContainerResponseDTO.from(chatContainer, previousParticipantIds, participant);
    }

    public synchronized RemoveChatContainerResponseDTO removeContainer(Long gameId, Long containerId) {
        ChatContainer previous = containerRepository.deleteByGameIdAndContainerId(gameId, containerId);
        if (previous != null) {
            return RemoveChatContainerResponseDTO.from(previous.getGameId(), previous.getContainerId(), previous.getParticipantIds());
        } else {
            return RemoveChatContainerResponseDTO.from(-1L, -1L, new ArrayList<>());
        }
    }
}

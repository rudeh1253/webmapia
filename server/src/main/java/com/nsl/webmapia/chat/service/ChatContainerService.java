package com.nsl.webmapia.chat.service;

import com.nsl.webmapia.chat.domain.ChatContainer;
import com.nsl.webmapia.chat.dto.CreationNewChatContainerResponseDTO;
import com.nsl.webmapia.chat.dto.NewParticipantResponseDTO;
import com.nsl.webmapia.chat.dto.RemoveChatContainerResponseDTO;
import com.nsl.webmapia.chat.repository.ChatContainerRepository;
import com.nsl.webmapia.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ChatContainerService {
    private ChatContainerRepository containerRepository;

    @Autowired
    public ChatContainerService(ChatContainerRepository containerRepository) {
        this.containerRepository = containerRepository;
    }

    public CreationNewChatContainerResponseDTO createContainer(Long gameId, String containerName, List<Long> participants) {
        Random random = new Random();
        Long containerId = (Math.abs(random.nextLong()) % 1000000) + 2;
        ChatContainer newContainer = new ChatContainer(gameId, containerId, containerName, participants);
        containerRepository.save(newContainer);
        return CreationNewChatContainerResponseDTO.from(newContainer);
    }

    public RemoveChatContainerResponseDTO removeContainer(Long gameId, Long containerId) {
        ChatContainer chatContainer = containerRepository
                .findByGameIdAndContainerId(gameId, containerId)
                .orElse(new ChatContainer(gameId, containerId, "", new ArrayList<>()));
        containerRepository.deleteByGameIdAndContainerId(gameId, containerId);
        return RemoveChatContainerResponseDTO.from(chatContainer);
    }

    public NewParticipantResponseDTO addNewParticipant(Long gameId, Long containerId, Long newParticipantId) {
        Optional<ChatContainer> chatContainerWrapper = containerRepository.findByGameIdAndContainerId(gameId, containerId);
        List<Long> receivers = null;
        if (chatContainerWrapper.isEmpty()) {
            List<Long> newParticipantList = new ArrayList<>();
            newParticipantList.add(newParticipantId);
            this.containerRepository.save(new ChatContainer(gameId, containerId, "undefined chat", newParticipantList));
            receivers = newParticipantList;
        } else {
            ChatContainer chatContainer = chatContainerWrapper.get();
            chatContainer.getParticipantIds().add(newParticipantId);
            receivers = chatContainer.getParticipantIds();
        }
        return NewParticipantResponseDTO.from(gameId, containerId, newParticipantId, receivers);
    }
}

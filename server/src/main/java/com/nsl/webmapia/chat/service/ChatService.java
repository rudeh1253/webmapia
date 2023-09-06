package com.nsl.webmapia.chat.service;

import com.nsl.webmapia.chat.domain.ChatContainer;
import com.nsl.webmapia.chat.domain.PrivateChatMessage;
import com.nsl.webmapia.chat.domain.PublicChatMessage;
import com.nsl.webmapia.chat.repository.ChatContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatService {
    private ChatContainerRepository containerRepository;

    @Autowired
    public ChatService(ChatContainerRepository containerRepository) {
        this.containerRepository = containerRepository;
    }

    public PublicChatMessage sendPublicMessage(PublicChatMessage chat) {
        return chat;
    }

    public Map<Long, PrivateChatMessage> sendPrivateChatMessage(PrivateChatMessage chat) {
        Optional<ChatContainer> chatContainerOptional =
                containerRepository.findByGameIdAndContainerId(chat.getGameId(), chat.getContainerId());
        if (chatContainerOptional.isEmpty()) {
            return new HashMap<>();
        } else {
            ChatContainer chatContainer = chatContainerOptional.get();
            Map<Long, PrivateChatMessage> privateMessageMap = new HashMap<>();
            chatContainer.getParticipantIds().stream()
                    .forEach(par -> privateMessageMap.put(par, chat));
            return privateMessageMap;
        }
    }
}

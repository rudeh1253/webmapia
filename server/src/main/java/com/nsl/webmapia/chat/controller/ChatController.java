package com.nsl.webmapia.chat.controller;

import com.nsl.webmapia.chat.domain.PrivateChatMessage;
import com.nsl.webmapia.chat.domain.PublicChatMessage;
import com.nsl.webmapia.chat.dto.*;
import com.nsl.webmapia.chat.service.ChatContainerService;
import com.nsl.webmapia.chat.service.ChatService;
import com.nsl.webmapia.common.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller for chatting
 */
@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatContainerService containerService;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate,
                          ChatService chatService,
                          ChatContainerService containerService) {
        this.messagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
        this.containerService = containerService;
    }

    @MessageMapping("/chatroom/public-message")
    public void receivePublicMessage(@Payload PublicChatMessage publicChatMessage) {
        messagingTemplate.convertAndSend("/chatroom/" + publicChatMessage.getGameId(),
                CommonResponse.ok(publicChatMessage, LocalDateTime.now()));
    }

    @MessageMapping("/chatroom/private-message")
    public void receivePrivateMessage(@Payload PrivateChatMessage privateChatMessage) {
        Map<Long, PrivateChatMessage> messages = chatService.sendPrivateChatMessage(privateChatMessage);
        for (Long receiverId : messages.keySet()) {
            messagingTemplate.convertAndSend("/chatroom/" + privateChatMessage.getGameId() + "/private/" + receiverId,
                    CommonResponse.ok(privateChatMessage, LocalDateTime.now()));
        }
    }

    @MessageMapping("/chatroom/participate-chat-container")
    public void participateChatContainer(@Payload ParticipateChatContainerRequestDTO request) {
        ParticipateChatContainerResponseDTO response =
                containerService.participateChatContainer(request);
        List<Long> receiverIds = response.getPreviousParticipants();
        synchronized (this) {
            receiverIds.forEach(id -> messagingTemplate.convertAndSend("/notification/private/" + response.getGameId() + "/" + id,
                    CommonResponse.ok(response, LocalDateTime.now())));
            messagingTemplate.convertAndSend("/notification/private/" + response.getGameId() + "/" + response.getNewParticipant(),
                    CommonResponse.ok(response, LocalDateTime.now()));
        }
    }

    @MessageMapping("/chatroom/remove-chat-container")
    public void removeChatContainer(@Payload RemoveChatContainerRequestDTO request) {
        RemoveChatContainerResponseDTO response =
                containerService.removeContainer(request.getGameId(), request.getContainerId());
        response.getParticipants()
                .forEach(id -> messagingTemplate.convertAndSend("/notification/private/" + response.getGameId() + "/" + id,
                        CommonResponse.ok(response, LocalDateTime.now())));
    }
}

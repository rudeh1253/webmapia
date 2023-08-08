package com.nsl.webmapia.chat.controller;

import com.nsl.webmapia.chat.domain.PrivateChatMessage;
import com.nsl.webmapia.chat.domain.PublicChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Controller for chatting
 */
@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.messagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chatroom/public-message")
    public void receivePublicMessage(@Payload PublicChatMessage publicChatMessage) {
        System.out.println("publicChatMessage = " + publicChatMessage);
        messagingTemplate.convertAndSend("/chatroom/" + publicChatMessage.getGameId(), publicChatMessage);
    }

    @MessageMapping("/chatroom/private-message")
    public void receivePrivateMessage(@Payload PrivateChatMessage privateChatMessage) {
        System.out.println("privateChatMessage = " + privateChatMessage);
        privateChatMessage.getReceiverUserIds().forEach(id -> {
            messagingTemplate.convertAndSend("/chatroom/" + privateChatMessage.getGameId() + "/private/" + id);
        });
    }
}

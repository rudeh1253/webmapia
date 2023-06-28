package com.nsl.webmapia.message.controller;

import com.nsl.webmapia.message.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receivePublicMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public Message receivePrivateMessage(@Payload Message message) {
        messagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        return message;
    }
}

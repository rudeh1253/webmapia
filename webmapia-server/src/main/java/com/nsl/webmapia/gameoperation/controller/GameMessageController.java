package com.nsl.webmapia.gameoperation.controller;

import com.nsl.webmapia.gameoperation.domain.GameSetting;
import com.nsl.webmapia.gameoperation.dto.GameStartNotificationDTO;
import com.nsl.webmapia.gameoperation.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameMessageController {
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameMessageController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping
    public void gameStart(@Payload GameStartNotificationDTO request) {
        messagingTemplate.convertAndSend("/notification/public" + request.getGameId(), request);
    }
}

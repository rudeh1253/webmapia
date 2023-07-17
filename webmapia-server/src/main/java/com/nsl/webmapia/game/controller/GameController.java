package com.nsl.webmapia.game.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.common.exception.ErrorCode;
import com.nsl.webmapia.common.exception.UnsupportedNotificationTypeException;
import com.nsl.webmapia.game.domain.GameNotificationType;
import com.nsl.webmapia.game.dto.request.UserRequestDTO;
import com.nsl.webmapia.game.dto.response.UserResponseDTO;
import com.nsl.webmapia.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class GameController {
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/enter-user")
    @SendTo("/notification/public")
    public ResponseEntity<CommonResponse> addUser(@Payload UserRequestDTO request) {
        if (request.getNotificationType() != GameNotificationType.USER_ENTERED) {
            throw new UnsupportedNotificationTypeException(ErrorCode.INVALID_INPUT_TYPE);
        }
        final Long gameId = request.getGameId();
        gameService.addUser(gameId, request.getUserId(), request.getUsername());
        List<UserResponseDTO> users = gameService.getAllUsers(gameId);
        return CommonResponse.ok(users, LocalDateTime.now());
    }

    @MessageMapping("/game/user-exit")
    @SendTo

    @Scheduled(fixedRate = 1000)
    public void notifyOneSecondElapsed() {
        messagingTemplate.convertAndSend("/notification/public", CommonResponse.ok(GameNotificationType.ONE_SECOND_ELAPSED, LocalDateTime.now()));
    }
}

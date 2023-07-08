package com.nsl.webmapia.game.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.common.exception.ErrorCode;
import com.nsl.webmapia.common.exception.UnsupportedNotificationTypeException;
import com.nsl.webmapia.game.domain.notification.GameNotification;
import com.nsl.webmapia.game.domain.notification.GameNotificationType;
import com.nsl.webmapia.game.dto.UserDTO;
import com.nsl.webmapia.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class GameController {
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/notification/enter-user")
    public ResponseEntity<CommonResponse> addUser(@Payload UserDTO userDTO) {
        if (userDTO.getGameNotificationType() != GameNotificationType.USER_ENTERED) {
            throw new UnsupportedNotificationTypeException(ErrorCode.INVALID_INPUT_TYPE);
        }
        final Long gameId = userDTO.getGameId();
        gameService.addUser(gameId, userDTO.getTargetUserId());
        gameService.getAllUsers(gameId)
                .forEach(user -> {
                    messagingTemplate.convertAndSendToUser(String.valueOf(user.getID()), "/private", CommonResponse.ok(
                            new UserDTO(GameNotificationType.USER_ENTERED, gameId, -1L, userDTO.getTargetUserId()),
                            LocalDateTime.now()
                    ));
                });
        return CommonResponse.ok(userDTO, LocalDateTime.now());
    }
}

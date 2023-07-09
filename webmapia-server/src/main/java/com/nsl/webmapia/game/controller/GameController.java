package com.nsl.webmapia.game.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.common.exception.ErrorCode;
import com.nsl.webmapia.common.exception.UnsupportedNotificationTypeException;
import com.nsl.webmapia.game.domain.notification.GameNotificationType;
import com.nsl.webmapia.game.dto.UserResponseDTO;
import com.nsl.webmapia.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class GameController {
//    private GameService gameService;
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
//        this.gameService = gameService;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @MessageMapping("/game/notification/enter-user")
//    public ResponseEntity<CommonResponse> addUser(@Payload UserResponseDTO userResponseDTO) {
//        if (userResponseDTO.getGameNotificationType() != GameNotificationType.USER_ENTERED) {
//            throw new UnsupportedNotificationTypeException(ErrorCode.INVALID_INPUT_TYPE);
//        }
//        final Long gameId = userResponseDTO.getGameId();
//        gameService.addUser(gameId, userResponseDTO.getTargetUserId());
//        gameService.getAllUsers(gameId)
//                .forEach(user -> {
//                    messagingTemplate.convertAndSendToUser(String.valueOf(user.getID()), "/private", CommonResponse.ok(
//                            new UserResponseDTO(GameNotificationType.USER_ENTERED, gameId, -1L, userResponseDTO.getTargetUserId()),
//                            LocalDateTime.now()
//                    ));
//                });
//        return CommonResponse.ok(userResponseDTO, LocalDateTime.now());
//    }
}

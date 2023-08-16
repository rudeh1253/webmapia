package com.nsl.webmapia.user.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.common.exception.ErrorCode;
import com.nsl.webmapia.common.exception.UnsupportedNotificationTypeException;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.user.dto.UserRequestDTO;
import com.nsl.webmapia.user.dto.UserResponseDTO;
import com.nsl.webmapia.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class UserMessageController {
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public UserMessageController(UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/user-enter")
    public void addUser(@Payload UserRequestDTO request) {
        System.out.println("request = " + request);
        if (request.getNotificationType() != NotificationType.USER_ENTERED) {
            throw new UnsupportedNotificationTypeException(ErrorCode.INVALID_INPUT_TYPE);
        }
        final Long gameId = request.getGameId();
        userService.addUser(gameId, request.getUserId(), request.getUsername());
        UserResponseDTO dto = userService.getUser(gameId, request.getUserId());
        dto.setNotificationType(NotificationType.USER_ENTERED);
        messagingTemplate.convertAndSend("/notification/public/" + gameId, CommonResponse.ok(dto, LocalDateTime.now()));
    }

    @MessageMapping("/game/user-exit")
    public void removeUser(@Payload UserRequestDTO request) {
        System.out.println("request = " + request);
        Long gameId = request.getGameId();
        Long userId = request.getUserId();
        UserResponseDTO dto = userService.removeUser(gameId, userId);
        System.out.println(userService.getAllUsers(gameId));
        messagingTemplate.convertAndSend("/notification/public/" + gameId, CommonResponse.ok(dto, LocalDateTime.now()));
    }
}

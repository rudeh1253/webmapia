package com.nsl.webmapia.gameoperation.controller;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.common.exception.ErrorCode;
import com.nsl.webmapia.common.exception.UnsupportedNotificationTypeException;
import com.nsl.webmapia.gameoperation.dto.CharacterGenerationRequestDTO;
import com.nsl.webmapia.gameoperation.dto.GameStartNotificationDTO;
import com.nsl.webmapia.gameoperation.dto.VoteRequestDTO;
import com.nsl.webmapia.gameoperation.service.GameService;
import com.nsl.webmapia.gameoperation.dto.CharacterGenerationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameMessageController {
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameMessageController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/start")
    public void gameStart(@Payload GameStartNotificationDTO request) {
        messagingTemplate.convertAndSend("/notification/public" + request.getGameId(), request);
    }

    @MessageMapping("/game/distribute-character")
    public void generateCharacters(@Payload CharacterGenerationRequestDTO request) {
        if (request.getNotificationType() != NotificationType.CHARACTER_GENERATION) {
            throw new UnsupportedNotificationTypeException(ErrorCode.INVALID_INPUT_TYPE);
        }
        List<CharacterGenerationResponseDTO> result =
                gameService.generateCharacters(request.getGameId(), request.getCharacterDistribution());
        result.forEach(dto -> {
            String dest = "/notification/private/" + dto.getGameId() + "/" + dto.getReceiverId();
            messagingTemplate.convertAndSend(dest, dto);
        });
    }

    @MessageMapping("/game/vote")
    public void vote(@Payload VoteRequestDTO request) {
        if (request.getNotificationType() != NotificationType.VOTE) {
            throw new UnsupportedNotificationTypeException(ErrorCode.INVALID_INPUT_TYPE);
        }
        gameService.acceptVote(request.getGameId(), request.getVoterId(), request.getSubjectId());
    }
}

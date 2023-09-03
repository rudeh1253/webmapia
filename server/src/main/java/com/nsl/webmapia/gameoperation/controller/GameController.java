package com.nsl.webmapia.gameoperation.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.common.exception.ErrorCode;
import com.nsl.webmapia.common.exception.UnsupportedNotificationTypeException;
import com.nsl.webmapia.gameoperation.dto.*;
import com.nsl.webmapia.gameoperation.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class GameController {
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/start")
    public void gameStart(@Payload GameStartNotificationDTO request) {
        System.out.println("request = " + request);
        messagingTemplate.convertAndSend("/notification/public/" + request.getGameId(), CommonResponse.ok(request, LocalDateTime.now()));
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
            messagingTemplate.convertAndSend(dest, CommonResponse.ok(dto, LocalDateTime.now()));
        });
    }

    @MessageMapping("/game/end-phase")
    public void endPhase(@Payload PhaseEndRequestDTO request) {
        PhaseEndNotificationDTO result = gameService.phaseEnd(request.getGameId(), request.getUserId());
        if (result.isEnd()) {
            messagingTemplate.convertAndSend("/notification/public/" + result.getGameId(), CommonResponse.ok(result, LocalDateTime.now()));
        }
    }

    @MessageMapping("/game/post-phase")
    public void postPhase(@Payload PostPhaseRequestDTO request) {
        PhaseResultDTO phaseResultDTO = gameService.postPhase(request.getGameId());
        messagingTemplate.convertAndSend("/notification/public/" + phaseResultDTO.getGameId(), CommonResponse.ok(phaseResultDTO, LocalDateTime.now()));
    }

    @MessageMapping("/game/vote")
    public void vote(@Payload VoteRequestDTO request) {
        if (request.getNotificationType() != NotificationType.VOTE) {
            throw new UnsupportedNotificationTypeException(ErrorCode.INVALID_INPUT_TYPE);
        }
        gameService.acceptVote(request.getGameId(), request.getVoterId(), request.getSubjectId());
    }

    @GetMapping("/game/current-phase/{gameId}")
    public CurrentPhaseResponseDTO getCurrentPhase(@PathVariable("gameId") Long gameId) {
        return CurrentPhaseResponseDTO.of(gameId, gameService.getCurrentPhase(gameId));
    }
}

package com.nsl.webmapia.gameoperation.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GamePhase;
import com.nsl.webmapia.gameoperation.dto.*;
import com.nsl.webmapia.gameoperation.service.GameService;
import com.nsl.webmapia.skill.dto.ProcessSkillsRequestDTO;
import com.nsl.webmapia.skill.dto.SkillActivationRequestDTO;
import com.nsl.webmapia.skill.dto.SkillResultResponseDTO;
import com.nsl.webmapia.user.dto.UserResponseDTO;
import com.nsl.webmapia.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;
    private UserService userService;

    @Autowired
    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @MessageMapping("/game/start")
    public void gameStart(@Payload GameStartRequestDTO request) {
        GameStartResponseDTO dto = this.gameService.gameStart(request);
        messagingTemplate.convertAndSend("/notification/public/" + request.getGameId(),
                CommonResponse.ok(dto, LocalDateTime.now()));
    }

    @MessageMapping("/game/distribute-character")
    public void generateCharacters(@Payload CharacterGenerationRequestDTO request) {
        List<CharacterGenerationResponseDTO> result =
                gameService.generateCharacters(request.getGameId(), request.getCharacterDistribution());
        result.forEach(dto -> {
            String dest = "/notification/private/" + dto.getGameId() + "/" + dto.getReceiverId();
            messagingTemplate.convertAndSend(dest, CommonResponse.ok(dto, LocalDateTime.now()));
        });
    }

    @MessageMapping("/game/end-phase")
    public void endPhase(@Payload PhaseEndRequestDTO request) {
        boolean hasEnded = gameService.phaseEnd(request.getGameId(), request.getUserId());
        if (hasEnded) {
            Map<Long, PhaseResultResponseDTO> response = gameService.postPhase(request);
            response.keySet().forEach(receiverId -> {
                messagingTemplate.convertAndSend("/notification/private/"
                        + request.getGameId() + "/" + receiverId, CommonResponse.ok(response.get(receiverId), LocalDateTime.now()));
            });
        }
    }

    @MessageMapping("/game/end-game")
    public void endGame(@Payload GameEndRequestDTO request) {
        boolean hasEnded = gameService.gameEnd(request.getGameId(), request.getUserId());
        if (hasEnded) {
            gameService.clearCurrentGame(request.getGameId());
            messagingTemplate.convertAndSend("/notification/public/" + request.getGameId(),
                    CommonResponse.ok(new GameEndResponseDTO(request.getGameId()), LocalDateTime.now()));
        }
    }

    @MessageMapping("/game/vote")
    public void vote(@Payload VoteRequestDTO request) {
        gameService.acceptVote(request.getGameId(), request.getVoterId(), request.getSubjectId());
    }

    @MessageMapping("/game/activate-skill")
    public void activateSkill(@Payload SkillActivationRequestDTO request) {
        gameService.activateSkill(request.getGameId(), request.getActivatorId(), request.getTargetId(), request.getSkillType());
    }

    @GetMapping("/game/current-phase/{gameId}")
    public CurrentPhaseResponseDTO getCurrentPhase(@PathVariable("gameId") Long gameId) {
        return CurrentPhaseResponseDTO.of(gameId, gameService.getCurrentPhase(gameId));
    }
}

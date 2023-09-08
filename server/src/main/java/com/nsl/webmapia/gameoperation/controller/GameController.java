package com.nsl.webmapia.gameoperation.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.dto.*;
import com.nsl.webmapia.gameoperation.service.GameService;
import com.nsl.webmapia.skill.dto.ProcessSkillsRequestDTO;
import com.nsl.webmapia.skill.dto.SkillActivationRequestDTO;
import com.nsl.webmapia.skill.dto.SkillResultResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class GameController {
    // TODO: This should be fixed. This is the result of my laziness
    private boolean skillProcessing;

    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.skillProcessing = false;
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/start")
    public void gameStart(@Payload GameStartRequestDTO request) {
        messagingTemplate.convertAndSend("/notification/public/" + request.getGameId(),
                CommonResponse.ok(GameStartResponseDTO.from(request.getGameSetting(), request.getGameId()), LocalDateTime.now()));
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
        PhaseEndResponseDTO result = gameService.phaseEnd(request.getGameId(), request.getUserId());
        System.out.println("PhaseEnd = " + result);
        if (result.isEnd()) {
            this.skillProcessing = false;
            messagingTemplate.convertAndSend("/notification/public/" + result.getGameId(), CommonResponse.ok(result, LocalDateTime.now()));
        }
    }

    @MessageMapping("/game/post-phase")
    public void postPhase(@Payload PostPhaseRequestDTO request) {
        PhaseResultResponseDTO phaseResultResponseDTO = gameService.postPhase(request.getGameId());
        System.out.println("phaseResultDTO = " + phaseResultResponseDTO);
        messagingTemplate.convertAndSend("/notification/private/" + phaseResultResponseDTO.getGameId() + "/" + request.getUserId(), CommonResponse.ok(phaseResultResponseDTO, LocalDateTime.now()));
    }

    @MessageMapping("/game/vote")
    public void vote(@Payload VoteRequestDTO request) {
        gameService.acceptVote(request.getGameId(), request.getVoterId(), request.getSubjectId());
    }

    @MessageMapping("/game/activate-skill")
    public void activateSkill(@Payload SkillActivationRequestDTO request) {
        gameService.activateSkill(request.getGameId(), request.getActivatorId(), request.getTargetId(), request.getSkillType());
    }

    @MessageMapping("/game/process-skill")
    public void processSkill(@Payload ProcessSkillsRequestDTO request) {
        if (!skillProcessing) {
            skillProcessing = true;
            List<SkillResultResponseDTO> skillResults = gameService.processSkills(request.getGameId());
            for (SkillResultResponseDTO note : skillResults) {
                switch (note.getNotificationType()) {
                    case SKILL_PUBLIC:
                        messagingTemplate.convertAndSend("/notification/public/" + note.getGameId(), CommonResponse.ok(note, LocalDateTime.now()));
                        break;
                    case SKILL_PRIVATE:
                        messagingTemplate.convertAndSend("/notification/private/" + note.getGameId() + "/" + note.getReceiverId(),
                                CommonResponse.ok(note, LocalDateTime.now()));
                        break;
                }
            }
        }
    }

    @GetMapping("/game/current-phase/{gameId}")
    public CurrentPhaseResponseDTO getCurrentPhase(@PathVariable("gameId") Long gameId) {
        return CurrentPhaseResponseDTO.of(gameId, gameService.getCurrentPhase(gameId));
    }
}

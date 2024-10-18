package nsl.webmapia.game.domain.gameoperation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.gameoperation.dto.request.CharacterDistributionRequestDto;
import nsl.webmapia.game.domain.gameoperation.dto.request.GameStartRequestDto;
import nsl.webmapia.game.domain.gameoperation.dto.request.PhaseEndRequestDto;
import nsl.webmapia.game.domain.gameoperation.dto.response.CharacterDistributionResponseDto;
import nsl.webmapia.game.domain.gameoperation.dto.response.GameStartResponseDto;
import nsl.webmapia.game.domain.gameoperation.dto.response.PhaseResultResponseDto;
import nsl.webmapia.game.domain.gameoperation.service.GameService;
import nsl.webmapia.game.domain.gameoperation.service.PhaseResultService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameMessageController {
    private final GameService gameService;
    private final PhaseResultService phaseResultService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/game/phase-end-request")
    public void phaseEndRequest(@Payload PhaseEndRequestDto requestDto) {
        PhaseResultResponseDto phaseResultResponseDto =
                this.phaseResultService.endPhase(requestDto.getGameInstanceId(), requestDto.getRequesterId());
        if (phaseResultResponseDto.isEnded()) {
            this.messagingTemplate.convertAndSend(
                    "/topic/game-service/" + requestDto.getGameInstanceId() + "/phase-result",
                    phaseResultResponseDto
            );
        }
    }

    @MessageMapping("/game/start")
    public void startGame(@Payload GameStartRequestDto dto) {
        Integer gameInstanceId = this.gameService.startGame(dto.getGameRoomId());
        this.messagingTemplate.convertAndSend(
                "/topic/game-service/" + dto.getGameRoomId() + "/game-start",
                new GameStartResponseDto(gameInstanceId)
        );
    }

    @MessageMapping("/game/characters/distribute")
    public void distributeCharacters(@Payload CharacterDistributionRequestDto dto) {
        CharacterDistributionResponseDto result = this.gameService.distributeCharacters(dto);
        this.messagingTemplate.convertAndSend(
                "/topic/game-service/" + result.getGameInstanceId() + "/character-distribution",
                result
        );
    }
}

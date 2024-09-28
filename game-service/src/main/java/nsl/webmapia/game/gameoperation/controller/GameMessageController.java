package nsl.webmapia.game.gameoperation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.dto.request.PhaseEndRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;
import nsl.webmapia.game.gameoperation.service.GameService;
import nsl.webmapia.game.gameoperation.service.PhaseResultService;
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
}

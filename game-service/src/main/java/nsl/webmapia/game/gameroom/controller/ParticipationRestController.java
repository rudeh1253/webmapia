package nsl.webmapia.game.gameroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.common.rest.BaseResponse;
import nsl.webmapia.game.gameroom.dto.request.ParticipationRequestDto;
import nsl.webmapia.game.gameroom.dto.response.ParticipationResponseDto;
import nsl.webmapia.game.gameroom.service.ParticipationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParticipationRestController {
    private final ParticipationService participationService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/game/rooms/{roomId}/participate")
    public ResponseEntity<BaseResponse<ParticipationResponseDto>> participate(@PathVariable("roomId") int roomId,
                                                                              @RequestBody ParticipationRequestDto dto) {
        ParticipationResponseDto result = this.participationService.participate(roomId, dto.getNewParticipantId());
        this.messagingTemplate.convertAndSend("/topic/game-service/rooms/" + roomId, result);
        log.info("roomId={}", roomId);
        return new ResponseEntity<>(
                BaseResponse.created("Successfully participated", result),
                HttpStatus.CREATED
        );
    }
}

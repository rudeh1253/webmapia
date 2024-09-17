package nsl.webmapia.game.vote.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.common.rest.BaseResponse;
import nsl.webmapia.game.vote.dto.VoteDto;
import nsl.webmapia.game.vote.dto.request.VoteRequestDto;
import nsl.webmapia.game.vote.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/vote")
public class VoteMessageController {
    private final VoteService voteService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> vote(@RequestBody VoteRequestDto dto) {
        List<VoteDto> result = this.voteService.vote(dto);
        this.messagingTemplate.convertAndSend(
                "/topic/game-service/" + dto.getGameInstanceId() + "/vote",
                result
        );
        return ResponseEntity.ok(
                BaseResponse.ok("Successfully voted")
        );
    }
}

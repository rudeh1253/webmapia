package nsl.webmapia.game.domain.vote.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.vote.dto.VoteDto;
import nsl.webmapia.game.domain.vote.dto.request.VoteRequestDto;
import nsl.webmapia.game.domain.vote.service.VoteService;
import nsl.webmapia.game.global.rest.BaseResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class VoteMessageController {
    private final VoteService voteService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/vote/do")
    public void vote(@Payload VoteRequestDto dto) {
        log.info("dto={}", dto);
        List<VoteDto> result = this.voteService.vote(dto);
        this.messagingTemplate.convertAndSend(
                "/topic/game-service/" + dto.getGameInstanceId() + "/vote",
                BaseResponse.ok("Successfully voted", result)
        );
    }
}

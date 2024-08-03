package nsl.webmapia.game.gameoperation.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

    @Override
    public BaseSystemMessageResponseDto<Void> startGame(int roomId) {
        return null;
    }

    @Override
    public BaseSystemMessageResponseDto<List<VoteDto>> vote(VoteRequestDto voteRequestDto) {
        return null;
    }

    @Override
    public BaseSystemMessageResponseDto<PhaseResultResponseDto> endPhase(int roomId, String requesterId) {
        return null;
    }
}

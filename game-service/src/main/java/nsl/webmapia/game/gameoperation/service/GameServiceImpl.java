package nsl.webmapia.game.gameoperation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.common.SystemMessageType;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.VoteRepository;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.service.GameRoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    private final GameInstanceRepository gameInstanceRepository;
    private final VoteRepository voteRepository;
    private final GameRoomService gameRoomService;
    private final MessageSource messageSource;

    @Override
    public BaseSystemMessageResponseDto<Object> startGame(int roomId) {
        GameRoom gameRoom = new GameRoom();
        BeanUtils.copyProperties(this.gameRoomService.getGameRoom(roomId), gameRoom);

        GameInstance gameInstance = new GameInstance();
        gameInstance.setGameRoom(gameRoom);
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        this.gameInstanceRepository.save(gameInstance);
        return BaseSystemMessageResponseDto.builder()
                .receiverIds(gameRoom.getParticipationList().stream().map(Participation::getParticipantId).toList())
                .systemMessageType(SystemMessageType.GAME_STARTED)
                .message(this.messageSource.getMessage("system.alert.game-started", null, null))
                .content(null)
                .build();
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

package nsl.webmapia.game.gameroom.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.common.ErrorCode;
import nsl.webmapia.game.gameoperation.service.GameService;
import nsl.webmapia.game.gameroom.dto.ParticipationDto;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.exception.UnableToEnterGameRoomException;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final GameService gameService;
    private final GameRoomRepository gameRoomRepository;

    public ParticipationDto participate(int gameRoomId, String participantId) {
        if (this.gameService.hasGameStarted(gameRoomId)) {
            throw new UnableToEnterGameRoomException(ErrorCode.GAME_ALREADY_STARTED);
        }
        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId)
                .orElseThrow(NoSuchElementException::new);
        Participation newParticipation = new Participation(participantId, gameRoom);
        this.participationRepository.save(newParticipation);
        return new ParticipationDto(
                gameRoomId,
                newParticipation.getParticipantId(),
                gameRoom.getParticipationList().stream()
                        .map(Participation::getParticipantId)
                        .toList()
        );
    }
}

package nsl.webmapia.game.domain.gameroom.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.gameoperation.service.GameService;
import nsl.webmapia.game.domain.gameroom.dto.response.ParticipationResponseDto;
import nsl.webmapia.game.domain.gameroom.entity.GameRoom;
import nsl.webmapia.game.domain.gameroom.entity.Participation;
import nsl.webmapia.game.domain.gameroom.exception.UnableToEnterGameRoomException;
import nsl.webmapia.game.domain.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.domain.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.domain.member.dto.MemberDto;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.global.ErrorCode;
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

    public ParticipationResponseDto participate(int gameRoomId, String participantId) {
        return participate(gameRoomId, participantId, false);
    }

    public ParticipationResponseDto participate(int gameRoomId, String participantId, boolean isHost) {
        if (this.gameService.hasGameStarted(gameRoomId)) {
            throw new UnableToEnterGameRoomException(ErrorCode.GAME_ALREADY_STARTED);
        }
        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId)
                .orElseThrow(NoSuchElementException::new);
        Participation newParticipation = new Participation(new Member(participantId), isHost, gameRoom);
        this.participationRepository.save(newParticipation);
        return new ParticipationResponseDto(
                gameRoomId,
                MemberDto.of(newParticipation.getParticipant()),
                gameRoom.getParticipationList().stream()
                        .map((p) -> MemberDto.of(p.getParticipant()))
                        .toList()
        );
    }
}

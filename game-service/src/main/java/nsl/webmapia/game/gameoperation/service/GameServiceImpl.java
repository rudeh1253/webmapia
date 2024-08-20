package nsl.webmapia.game.gameoperation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.entity.Vote;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.VoteRepository;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    private final GameInstanceRepository gameInstanceRepository;
    private final VoteRepository voteRepository;
    private final GameRoomRepository gameRoomRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private final CharacterDefinitionFactoryService characterDefinitionFactoryService;
    private final MessageSource messageSource;

    @Override
    public Integer startGame(int roomId) {
        // TODO: NoSuchElementException should notify that GameRoom instance of roomId is absent.
        // Or replace with another exception.
        GameRoom gameRoom = this.gameRoomRepository.findById(roomId)
                .orElseThrow(NoSuchElementException::new);

        GameInstance gameInstance = new GameInstance();
        gameInstance.setGameRoom(gameRoom);
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        this.gameInstanceRepository.save(gameInstance);
        return gameInstance.getGameInstanceId();
    }

    @Override
    public List<VoteDto> vote(VoteRequestDto requestDto) {
        GameInstance gameInstance = this.gameInstanceRepository.findById(requestDto.getGameInstanceId())
                .orElseThrow(NoSuchElementException::new);
        // TODO: instead of IllegalArgumentException, more specific exception is needed.
        // The exception thrown here should be one that states no such Member of voterId isn't present.
        List<CharacterAssignment> characterAssignments = this.characterAssignmentRepository.findByGameInstanceId(gameInstance.getGameInstanceId());
        CharacterAssignment voterCharacterAssignment = characterAssignments.stream()
                .filter((ca) -> ca.getMemberId().equals(requestDto.getVoterId()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        CharacterDefinitionService characterDefinitionService =
                this.characterDefinitionFactoryService.getCharacterDefinitionOfCharacterCode(voterCharacterAssignment.getCharacterCode());
        int voteCount = characterDefinitionService.getVoteCount();
        this.voteRepository.save(new Vote(
                gameInstance.getRound(),
                requestDto.getVoterId(),
                requestDto.getTargetId(),
                voteCount,
                gameInstance
        ));

        return this.voteRepository.findByGameInstanceIdAndRound(gameInstance.getGameInstanceId(), gameInstance.getRound())
                .stream()
                .map(VoteDto::of)
                .toList();
    }

    @Override
    public PhaseResultResponseDto endPhase(int gameInstanceId, String requesterId) {
        return null;
    }
}

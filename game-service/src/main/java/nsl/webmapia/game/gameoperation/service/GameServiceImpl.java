package nsl.webmapia.game.gameoperation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.CharacterDistributionRequestDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.CharacterDistributionResponseDto;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.entity.Vote;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.VoteRepository;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    private final GameInstanceRepository gameInstanceRepository;
    private final VoteRepository voteRepository;
    private final GameRoomRepository gameRoomRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private final ParticipationRepository participationRepository;
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
        gameInstance.setRound(0);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        this.gameInstanceRepository.save(gameInstance);
        return gameInstance.getGameInstanceId();
    }

    @Override
    public CharacterDistributionResponseDto distributeCharacters(CharacterDistributionRequestDto dto)
            throws IllegalArgumentException {
        CharacterCode[] characterDist = numByCharactersToArray(dto.getNumByCharacters());
        List<Participation> participations =
                this.participationRepository.findNotDisconnectedByGameInstanceId(dto.getGameInstanceId());
        validateParameterOfDistributeCharacters(
                characterDist.length,
                participations.size()
        );

        List<String> participants = new ArrayList<>(participations.stream()
                .map(Participation::getParticipantId)
                .toList());
        Collections.shuffle(participants); // Randomness

        Map<String, CharacterCode> charactersAssigned = new HashMap<>();
        int characterDistLen = characterDist.length;
        GameInstance gameInstance = this.gameInstanceRepository.findById(dto.getGameInstanceId())
                .orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < participants.size(); i++) {
            String participant = participants.get(i);
            CharacterCode assignedCharacter = characterDistLen > i ? characterDist[i] : CharacterCode.CITIZEN;
            charactersAssigned.put(
                    participant,
                    assignedCharacter
            );
            CharacterAssignment ca = new CharacterAssignment();
            ca.setMemberId(participant);
            ca.setCharacterCode(assignedCharacter);
            ca.setLife(assignedCharacter == CharacterCode.SOLDIER ? 2 : 1);
            ca.setGameInstance(gameInstance);
            this.characterAssignmentRepository.save(ca);
        }
        return CharacterDistributionResponseDto.builder()
                .gameInstanceId(dto.getGameInstanceId())
                .characterCodesByMemberIds(charactersAssigned)
                .build();
    }

    private CharacterCode[] numByCharactersToArray(Map<CharacterCode, Integer> numByCharacters) {
        return numByCharacters.keySet()
                .stream()
                .flatMap((c) -> {
                    CharacterCode[] subArr = new CharacterCode[numByCharacters.get(c)];
                    Arrays.fill(subArr, c);
                    return Stream.of(subArr);
                })
                .toArray(CharacterCode[]::new);
    }

    private void validateParameterOfDistributeCharacters(
            int totalCharacterDistributionCount,
            int participationSize
    ) throws IllegalArgumentException {
        if (participationSize < totalCharacterDistributionCount) {
            throw new IllegalArgumentException(
                    "Total character distribution size exceeds participation size: "
                            + totalCharacterDistributionCount + " > " + participationSize
            );
        }
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
        GameInstance gameInstance = this.gameInstanceRepository.findById(gameInstanceId)
                .orElseThrow(IllegalArgumentException::new);

        return null;
    }

    private GamePhase getNextPhase(GamePhase currentPhase) {
        return switch (currentPhase) {
            case START, VOTE -> GamePhase.NIGHT;
            case DISCUSSION -> GamePhase.VOTE;
            case NIGHT -> GamePhase.DISCUSSION;
        };
    }
}

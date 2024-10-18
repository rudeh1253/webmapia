package nsl.webmapia.game.domain.gameoperation.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.character.domain.Faction;
import nsl.webmapia.game.domain.character.dto.CharacterAssignmentResultDto;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionService;
import nsl.webmapia.game.domain.gameoperation.domain.GamePhase;
import nsl.webmapia.game.domain.gameoperation.dto.GameInstanceDto;
import nsl.webmapia.game.domain.gameoperation.dto.request.CharacterDistributionRequestDto;
import nsl.webmapia.game.domain.gameoperation.dto.response.CharacterDistributionResponseDto;
import nsl.webmapia.game.domain.gameoperation.dto.response.GameResultResponseDto;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.domain.gameoperation.repository.GameInstanceUpdateDto;
import nsl.webmapia.game.domain.gameroom.entity.GameRoom;
import nsl.webmapia.game.domain.gameroom.entity.Participation;
import nsl.webmapia.game.domain.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.domain.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.domain.member.dto.MemberDto;
import nsl.webmapia.game.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class GameServiceImpl implements GameService {
    private final GameInstanceRepository gameInstanceRepository;
    private final GameRoomRepository gameRoomRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private final ParticipationRepository participationRepository;
    private final CharacterDefinitionFactoryService characterDefinitionFactoryService;
    private final MessageSource messageSource;
    private final Map<GamePhase, Integer> periodsByGamePhase;

    public GameServiceImpl(GameInstanceRepository gameInstanceRepository,
                           GameRoomRepository gameRoomRepository,
                           CharacterAssignmentRepository characterAssignmentRepository,
                           ParticipationRepository participationRepository,
                           CharacterDefinitionFactoryService characterDefinitionFactoryService,
                           MessageSource messageSource,
                           @Value("${game.default.phase-period-in-second.discussion}") int discussionPeriod,
                           @Value("${game.default.phase-period-in-second.night}") int nightPeriod,
                           @Value("${game.default.phase-period-in-second.vote}") int votePeriod) {
        this.gameInstanceRepository = gameInstanceRepository;
        this.gameRoomRepository = gameRoomRepository;
        this.characterAssignmentRepository = characterAssignmentRepository;
        this.participationRepository = participationRepository;
        this.characterDefinitionFactoryService = characterDefinitionFactoryService;
        this.messageSource = messageSource;
        this.periodsByGamePhase = Map.of(
                GamePhase.DISCUSSION, discussionPeriod,
                GamePhase.NIGHT, nightPeriod,
                GamePhase.VOTE, votePeriod
        );
    }

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

        List<Member> participants = new ArrayList<>(participations.stream()
                    .map(Participation::getParticipant)
                .toList());
        Collections.shuffle(participants); // Randomness

        Map<MemberDto, CharacterCode> charactersAssigned = new HashMap<>();
        int characterDistLen = characterDist.length;
        GameInstance gameInstance = this.gameInstanceRepository.findById(dto.getGameInstanceId())
                .orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < participants.size(); i++) {
            Member participant = participants.get(i);
            CharacterCode assignedCharacter = characterDistLen > i ? characterDist[i] : CharacterCode.CITIZEN;
            charactersAssigned.put(
                    MemberDto.of(participant),
                    assignedCharacter
            );
            CharacterAssignment ca = new CharacterAssignment();
            ca.setMember(participant);
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
    public GameInstanceDto getGameInstance(int gameInstanceId) {
        GameInstance gameInstance = this.gameInstanceRepository.findById(gameInstanceId)
                .orElseThrow(NoSuchElementException::new);
        return GameInstanceDto.of(gameInstance);
    }

    @Override
    public boolean hasGameStarted(int gameRoomId) {
        return this.gameInstanceRepository.existsAliveGameInstanceByGameRoomId(gameRoomId);
    }

    @Override
    public GameInstanceDto getAliveGameInstanceByRoomId(int gameRoomId) throws NoSuchElementException {
        return GameInstanceDto.of(this.gameInstanceRepository.findAliveGameInstanceByGameRoomId(gameRoomId)
                .orElseThrow(NoSuchElementException::new));
    }

    @Override
    public GamePhase proceedPhase(int gameInstanceId) {
        GameInstance gameInstance = this.gameInstanceRepository.findById(gameInstanceId)
                .orElseThrow(NoSuchElementException::new);
        GamePhase nextPhase = getNextPhase(gameInstance.getGamePhase());
        GameInstanceUpdateDto updateDto = GameInstanceUpdateDto.builder()
                .gameInstanceId(gameInstanceId)
                .gamePhase(nextPhase)
                .phaseEndTime(getNextPhaseEndTime(nextPhase))
                .build();
        this.gameInstanceRepository.updateByGameRoomId(updateDto);
        return nextPhase;
    }

    private GamePhase getNextPhase(GamePhase currentPhase) {
        return switch (currentPhase) {
            case START, VOTE -> GamePhase.NIGHT;
            case DISCUSSION -> GamePhase.VOTE;
            case NIGHT -> GamePhase.DISCUSSION;
            case END -> GamePhase.END;
        };
    }

    private LocalDateTime getNextPhaseEndTime(GamePhase nextPhase) {
        if (!this.periodsByGamePhase.containsKey(nextPhase)) {
            return null;
        }
        return LocalDateTime.now().plusSeconds(this.periodsByGamePhase.get(nextPhase));
    }

    @Override
    public GameResultResponseDto processGameResult(int gameInstanceId) {
        // TODO: Test code should be added later
        List<CharacterAssignment> characters = characterAssignmentRepository.findByGameInstanceId(gameInstanceId);
        List<CharacterAssignment> aliveCharacters = characters.stream()
                .filter((c) -> !c.isDead())
                .toList();
        Faction winnerFaction = computeWinner(
                getAliveCharactersOfFaction(aliveCharacters, Faction.WOLF),
                getAliveCharactersOfFaction(aliveCharacters, Faction.HUMAN),
                getAliveCharactersOfFaction(aliveCharacters, Faction.HUMAN_MOUSE)
        );
        if (winnerFaction == null) {
            return GameResultResponseDto.builder()
                    .gameEnded(false)
                    .build();
        }
        return GameResultResponseDto.builder()
                .gameEnded(true)
                .characterAssignments(
                        characters.stream()
                                .map(CharacterAssignmentResultDto::of)
                                .toList()
                )
                .winFaction(winnerFaction)
                .wolves(characters.stream().filter((c) -> belongsTo(c, Faction.WOLF)).map((c) -> MemberDto.of(c.getMember())).toList())
                .human(characters.stream().filter((c) -> belongsTo(c, Faction.HUMAN)).map((c) -> MemberDto.of(c.getMember())).toList())
                .humanMouse(characters.stream().filter((c) -> belongsTo(c, Faction.HUMAN_MOUSE)).map((c) -> MemberDto.of(c.getMember())).toList())
                .build();
    }

    private Set<CharacterCode> getAliveCharactersOfFaction(List<CharacterAssignment> aliveCharacters, Faction faction) {
        return aliveCharacters.stream()
                .filter((a) -> belongsTo(a, faction))
                .map(CharacterAssignment::getCharacterCode)
                .collect(Collectors.toSet());
    }

    private boolean belongsTo(CharacterAssignment character, Faction faction) {
        CharacterDefinitionService characterDefinitionService =
                this.characterDefinitionFactoryService.getCharacterDefinitionOfCharacterCode(character.getCharacterCode());
        return characterDefinitionService.getFaction() == faction;
    }

    private Faction computeWinner(
            Set<CharacterCode> aliveWolves,
            Set<CharacterCode> aliveHumans,
            Set<CharacterCode> aliveHumanMouse
    ) {
        Faction winnerFaction = null;
        if (aliveWolves.size() >= aliveHumans.size()) {
            winnerFaction = Faction.WOLF;
            if (aliveWolves.size() == 1 && aliveHumans.size() == 1 && aliveHumans.contains(CharacterCode.TEMPLAR)) {
                winnerFaction = Faction.HUMAN;
            }
            if (!aliveHumanMouse.isEmpty()) {
                winnerFaction = Faction.HUMAN_MOUSE;
            }
        }
        return winnerFaction;
    }
}

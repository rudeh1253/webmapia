package nsl.webmapia.game.gameoperation.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.dto.request.CharacterDistributionRequestDto;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.GameInstanceUpdateDto;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static nsl.webmapia.game.character.domain.CharacterCode.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class TestGameServiceImpl {

    @Autowired
    GameServiceImpl gameService;

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    CharacterAssignmentRepository characterAssignmentRepository;

    @Autowired
    GameInstanceRepository gameInstanceRepository;

    @DisplayName("startGame()")
    @Test
    void startGame() {
        int generatedGameRoomId = insertSampleGameRoom();

        assertThatNoException().isThrownBy(() -> {
            Integer generatedGameInstanceId = this.gameService.startGame(generatedGameRoomId);
            log.info("generatedGameInstanceId={}", generatedGameInstanceId);
        });
    }

    @DisplayName("startGame() - Attempt to start game of game room that doesn't exist")
    @Test
    void startGame_noSuchElementExceptionIsExpected() {
        int generatedGameRoomId = insertSampleGameRoom();

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> this.gameService.startGame(generatedGameRoomId + 1));
    }

    private int insertSampleGameRoom() {
        int generatedId = this.gameRoomRepository.save(getSampleGameRoom());
        Participation participation = new Participation(
                "sample-host",
                this.gameRoomRepository.findById(generatedId).get()
        );
        this.participationRepository.save(participation);
        return generatedId;
    }

    private GameRoom getSampleGameRoom() {
        GameRoom sampleGameRoom = new GameRoom();
        sampleGameRoom.setRoomName("sample-room");
        sampleGameRoom.setHostMemberId("sample-host");
        sampleGameRoom.setCreationTime(LocalDateTime.now());
        return sampleGameRoom;
    }

    static Stream<Arguments> distributeCharacters_noProblem() {
        List<String> participantIds = List.of(
                "sample1",
                "sample2",
                "sample3",
                "sample4",
                "sample5",
                "sample6",
                "sample7",
                "sample8",
                "sample9"
        );

        int participantsCount = participantIds.size() + 1; // + 1 means there is host not shown here.

        return Stream.of(
                Arguments.of(
                        Map.of(WOLF, 2),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(WOLF, 2),
                                new AbstractMap.SimpleEntry<>(CITIZEN, participantsCount - 2)
                        ),
                        participantIds
                ),
                Arguments.of(
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(WOLF, 2),
                                new AbstractMap.SimpleEntry<>(DETECTIVE, 1)
                        ),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(WOLF, 2),
                                new AbstractMap.SimpleEntry<>(DETECTIVE, 1),
                                new AbstractMap.SimpleEntry<>(CITIZEN, participantsCount - 3)
                        ),
                        participantIds
                ),
                Arguments.of(
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(WOLF, 2),
                                new AbstractMap.SimpleEntry<>(DETECTIVE, 1),
                                new AbstractMap.SimpleEntry<>(MEDIUMSHIP, 1),
                                new AbstractMap.SimpleEntry<>(SECRET_SOCIETY, 2),
                                new AbstractMap.SimpleEntry<>(NOBILITY, 1),
                                new AbstractMap.SimpleEntry<>(TEMPLAR, 1),
                                new AbstractMap.SimpleEntry<>(CITIZEN, 1)
                        ),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(WOLF, 2),
                                new AbstractMap.SimpleEntry<>(DETECTIVE, 1),
                                new AbstractMap.SimpleEntry<>(MEDIUMSHIP, 1),
                                new AbstractMap.SimpleEntry<>(SECRET_SOCIETY, 2),
                                new AbstractMap.SimpleEntry<>(NOBILITY, 1),
                                new AbstractMap.SimpleEntry<>(TEMPLAR, 1),
                                new AbstractMap.SimpleEntry<>(CITIZEN, participantsCount - 8)
                        ),
                        participantIds
                ),
                Arguments.of(
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(WOLF, 2),
                                new AbstractMap.SimpleEntry<>(DETECTIVE, 1),
                                new AbstractMap.SimpleEntry<>(MEDIUMSHIP, 1),
                                new AbstractMap.SimpleEntry<>(SECRET_SOCIETY, 2),
                                new AbstractMap.SimpleEntry<>(NOBILITY, 1),
                                new AbstractMap.SimpleEntry<>(TEMPLAR, 1),
                                new AbstractMap.SimpleEntry<>(CITIZEN, 1),
                                new AbstractMap.SimpleEntry<>(HUMAN_MOUSE, 1)
                        ),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(WOLF, 2),
                                new AbstractMap.SimpleEntry<>(DETECTIVE, 1),
                                new AbstractMap.SimpleEntry<>(MEDIUMSHIP, 1),
                                new AbstractMap.SimpleEntry<>(SECRET_SOCIETY, 2),
                                new AbstractMap.SimpleEntry<>(NOBILITY, 1),
                                new AbstractMap.SimpleEntry<>(TEMPLAR, 1),
                                new AbstractMap.SimpleEntry<>(CITIZEN, participantsCount - 9),
                                new AbstractMap.SimpleEntry<>(HUMAN_MOUSE, 1)
                        ),
                        participantIds
                )
        );
    }

    @DisplayName("distributeCharacters - assigned without problem")
    @MethodSource
    @ParameterizedTest
    void distributeCharacters_noProblem(Map<CharacterCode, Integer> testcase,
                                        Map<CharacterCode, Integer> expected,
                                        List<String> given) {
        final int gameRoomId = insertSampleGameRoom();
        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId).get();

        given.forEach((p) -> this.participationRepository.save(new Participation(p, gameRoom)));
        final Integer gameInstanceId = this.gameService.startGame(gameRoomId);

        CharacterDistributionRequestDto requestDto = new CharacterDistributionRequestDto();
        requestDto.setGameInstanceId(gameInstanceId);
        requestDto.setNumByCharacters(testcase);

        Map<String, CharacterCode> result =
                this.gameService.distributeCharacters(requestDto).getCharacterCodesByMemberIds();
        result.forEach((k, v) -> log.info("{}={}", k, v));

        final String hostId = "sample-host";
        List<String> newIdList = new ArrayList<>(given);
        newIdList.add(hostId);
        assertThat(result.keySet()).containsExactlyInAnyOrder(newIdList.toArray(String[]::new));

        Map<CharacterCode, Integer> counter = new HashMap<>();
        result.forEach((k, v) -> {
            if (counter.containsKey(v)) {
                counter.put(v, counter.get(v) + 1);
            } else {
                counter.put(v, 1);
            }
        });

        assertThat(counter.keySet()).containsExactlyInAnyOrder(expected.keySet().toArray(new CharacterCode[0]));
        counter.forEach((k, v) -> assertThat(v).isEqualTo(expected.get(k)));

        result.forEach((k, v) -> {
            CharacterAssignment fromRepo =
                    this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(gameInstanceId, k).get();
            assertThat(fromRepo).isNotNull();
            assertThat(k).isEqualTo(fromRepo.getMemberId());
            assertThat(v).isEqualTo(fromRepo.getCharacterCode());
            assertThat(fromRepo.getLife()).isEqualTo(fromRepo.getCharacterCode() == SOLDIER ? 2 : 1);
        });
    }

    @DisplayName("distributeCharacter - IllegalArgumentException - character distribution request exceeds allowed")
    @Test
    void distributeCharacters_IllegalArgumentException() {
        List<String> given = List.of(
                "sample1",
                "sample2",
                "sample3",
                "sample4",
                "sample5",
                "sample6",
                "sample7",
                "sample8",
                "sample9"
        );

        final int gameRoomId = insertSampleGameRoom();
        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId).get();

        given.forEach((p) -> this.participationRepository.save(new Participation(p, gameRoom)));
        Integer gameInstanceId = this.gameService.startGame(gameRoomId);

        CharacterDistributionRequestDto requestDto = new CharacterDistributionRequestDto();
        requestDto.setGameInstanceId(gameInstanceId);
        requestDto.setNumByCharacters(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(WOLF, 2),
                new AbstractMap.SimpleEntry<>(DETECTIVE, 1),
                new AbstractMap.SimpleEntry<>(MEDIUMSHIP, 1),
                new AbstractMap.SimpleEntry<>(SECRET_SOCIETY, 2),
                new AbstractMap.SimpleEntry<>(NOBILITY, 1),
                new AbstractMap.SimpleEntry<>(TEMPLAR, 1),
                new AbstractMap.SimpleEntry<>(CITIZEN, given.size()),
                new AbstractMap.SimpleEntry<>(HUMAN_MOUSE, 1)
        ));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> this.gameService.distributeCharacters(requestDto));
    }

    @Value("${game.default.phase-period-in-second.discussion}") int discussionPeriod;
    @Value("${game.default.phase-period-in-second.night}") int nightPeriod;
    @Value("${game.default.phase-period-in-second.vote}") int votePeriod;

    @DisplayName("proceedPhase")
    @CsvSource(value = {
            "START:NIGHT",
            "NIGHT:DISCUSSION",
            "DISCUSSION:VOTE",
            "VOTE:NIGHT"
    }, delimiter = ':')
    @ParameterizedTest
    void proceedPhase(String currentPhaseName, String expectedNextPhaseName) {
        int generatedGameRoomId = insertSampleGameRoom();
        int generatedGameInstanceId = this.gameService.startGame(generatedGameRoomId);

        GamePhase currentPhase = GamePhase.valueOf(currentPhaseName);
        GamePhase expectedNextPhase = GamePhase.valueOf(expectedNextPhaseName);

        this.gameInstanceRepository.updateByGameRoomId(
                GameInstanceUpdateDto.builder()
                        .gameInstanceId(generatedGameInstanceId)
                        .gamePhase(currentPhase)
                        .build()
        );

        LocalDateTime now = LocalDateTime.now();
        GamePhase gamePhase = this.gameService.proceedPhase(generatedGameInstanceId);
        assertThat(gamePhase).isEqualTo(expectedNextPhase);

        GameInstance gameInstance = this.gameInstanceRepository.findById(generatedGameInstanceId).get();
        log.info("gameInstance={}", gameInstance);
        LocalDateTime expectedPhaseEndTime = now.plusSeconds(getPeriodPerGamePhase(gamePhase));
        log.info("gameInstance.phaseEndTime={}", gameInstance.getPhaseEndTime());
        log.info("expectedPhaseEndTime={}", expectedPhaseEndTime);
        assertThat(gameInstance.getGamePhase()).isEqualTo(expectedNextPhase);
        assertThat(gameInstance.getPhaseEndTime()).isAfterOrEqualTo(expectedPhaseEndTime);
        assertThat(gameInstance.getPhaseEndTime()).isAfterOrEqualTo(expectedPhaseEndTime);
    }

    int getPeriodPerGamePhase(GamePhase gamePhase) {
        return switch (gamePhase) {
            case VOTE -> this.votePeriod;
            case DISCUSSION ->  this.discussionPeriod;
            case NIGHT -> this.nightPeriod;
            default -> 0;
        };
    }
}
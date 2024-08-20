package nsl.webmapia.game.gameoperation.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.CharacterDistributionRequestDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.CharacterDistributionResponseDto;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.entity.Vote;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
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
        Integer gameInstanceId = this.gameService.startGame(gameRoomId);

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

    @DisplayName("vote()")
    @Test
    void vote() {
        String sampleHost = "sample-host";
        String[] sampleParticipants = {
                "sample-member01",
                "sample-member02",
                "sample-member03",
                "sample-member04",
                "sample-member05",
                "sample-member06",
                "sample-member07",
                "sample-member08",
                "sample-member09",
                "sample-member10",
                "sample-member11",
                "sample-member12",
                "sample-member13",
                "sample-member14"
        };

        CharacterCode[] characterAssignments = {
                WOLF,
                BETRAYER,
                FOLLOWER,
//                PREDICTOR,
                GUARD,
//                MEDIUMSHIP,
                DETECTIVE,
                SECRET_SOCIETY,
                NOBILITY,
                SOLDIER,
                TEMPLAR,
                CITIZEN,
                MURDERER,
                HUMAN_MOUSE,
                CITIZEN,
                CITIZEN
        };

        GameRoom sampleGameRoom = getSampleGameRoom();
        this.gameRoomRepository.save(sampleGameRoom);

        for (String sampleParticipant : sampleParticipants) {
            this.participationRepository.save(new Participation(sampleParticipant, sampleGameRoom));
        }

        this.gameService.startGame(sampleGameRoom.getRoomId());
        GameInstance gameInstance = this.gameInstanceRepository.findAliveGameInstanceByGameRoomId(sampleGameRoom.getRoomId()).get();

        this.characterAssignmentRepository.save(generateCharacterAssignment("sample-host", CITIZEN, gameInstance));
        for (int i = 0; i < sampleParticipants.length; i++) {
            this.characterAssignmentRepository.save(generateCharacterAssignment(
                    sampleParticipants[i], characterAssignments[i], gameInstance
            ));
        }

        List<VoteDto> voteDtoAccumulator = new ArrayList<>();
        voteDtoAccumulator.add(VoteDto.of(getVoteForGeneratingDto(sampleParticipants[0], sampleParticipants[1], gameInstance)));

        checkVote(
                sampleParticipants[0],
                sampleParticipants[1],
                voteDtoAccumulator,
                gameInstance.getGameInstanceId(),
                sampleParticipants
        );

        voteDtoAccumulator.add(VoteDto.of(getVoteForGeneratingDto(sampleParticipants[1], sampleParticipants[2], gameInstance)));

        checkVote(
                sampleParticipants[1],
                sampleParticipants[2],
                voteDtoAccumulator,
                gameInstance.getGameInstanceId(),
                sampleParticipants
        );

        voteDtoAccumulator.add(VoteDto.of(getVoteForGeneratingDto(sampleParticipants[5], sampleParticipants[4], gameInstance)));

        checkVote(
                sampleParticipants[5],
                sampleParticipants[4],
                voteDtoAccumulator,
                gameInstance.getGameInstanceId(),
                sampleParticipants
        );
    }

    private VoteRequestDto generateVoteRequestDto(int gameInstanceId, String voterId, String targetId) {
        VoteRequestDto voteRequestDto = new VoteRequestDto();
        voteRequestDto.setGameInstanceId(gameInstanceId);
        voteRequestDto.setVoterId(voterId);
        voteRequestDto.setTargetId(targetId);
        return voteRequestDto;
    }

    private CharacterAssignment generateCharacterAssignment(String memberId, CharacterCode characterCode, GameInstance gameInstance) {
        CharacterAssignment characterAssignment = new CharacterAssignment();
        characterAssignment.setMemberId(memberId);
        characterAssignment.setCharacterCode(characterCode);
        characterAssignment.setLife(characterCode == SOLDIER ? 2 : 1);
        characterAssignment.setGameInstance(gameInstance);
        return characterAssignment;
    }

    private void checkVote(String voterId,
                           String targetId,
                           List<VoteDto> expected,
                           int gameInstanceId,
                           String[] sampleParticipants) {
        List<VoteDto> votes =
                this.gameService.vote(generateVoteRequestDto(gameInstanceId, voterId, targetId));
        assertThat(votes.size()).isEqualTo(expected.size());
        assertThat(votes.stream().map(VoteDto::getVoterId).toArray(String[]::new))
                .containsExactlyInAnyOrder(expected.stream().map(VoteDto::getVoterId).toArray(String[]::new));
        assertThat(votes.stream().map(VoteDto::getTargetId).toArray(String[]::new))
                .containsExactlyInAnyOrder(expected.stream().map(VoteDto::getTargetId).toArray(String[]::new));
    }

    private Vote getVoteForGeneratingDto(String voterId, String targetId, GameInstance gameInstance) {
        return new Vote(0, voterId, targetId, 0, gameInstance);
    }
}
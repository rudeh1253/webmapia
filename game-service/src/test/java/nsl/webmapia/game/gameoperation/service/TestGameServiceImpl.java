package nsl.webmapia.game.gameoperation.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.common.SystemMessageType;
import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.entity.Vote;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static nsl.webmapia.game.character.domain.CharacterCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

        BaseSystemMessageResponseDto<Integer> responseDto = this.gameService.startGame(generatedGameRoomId);
        log.info("responseDto={}", responseDto);

        assertThat(responseDto.getReceiverIds()).contains("sample-host");
        assertThat(responseDto.getContent()).isNotNull();
        assertThat(responseDto.getContent()).isGreaterThan(0);
        assertThat(responseDto.getSystemMessageType()).isEqualTo(SystemMessageType.GAME_STARTED);
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
        BaseSystemMessageResponseDto<List<VoteDto>> vote =
                this.gameService.vote(generateVoteRequestDto(gameInstanceId, voterId, targetId));
        assertThat(vote.getSystemMessageType()).isEqualTo(SystemMessageType.VOTE_RESPONSE);
        assertThat(vote.getReceiverIds()).contains(sampleParticipants);
        assertThat(vote.getContent().size()).isEqualTo(expected.size());
        assertThat(vote.getContent().stream().map(VoteDto::getVoterId).toArray(String[]::new))
                .containsExactlyInAnyOrder(expected.stream().map(VoteDto::getVoterId).toArray(String[]::new));
        assertThat(vote.getContent().stream().map(VoteDto::getTargetId).toArray(String[]::new))
                .containsExactlyInAnyOrder(expected.stream().map(VoteDto::getTargetId).toArray(String[]::new));
    }

    private Vote getVoteForGeneratingDto(String voterId, String targetId, GameInstance gameInstance) {
        return new Vote(0, voterId, targetId, 0, gameInstance);
    }
}
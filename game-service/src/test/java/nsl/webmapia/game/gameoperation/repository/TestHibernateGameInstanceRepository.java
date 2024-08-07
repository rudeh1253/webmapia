package nsl.webmapia.game.gameoperation.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class TestHibernateGameInstanceRepository {

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    HibernateGameInstanceRepository gameInstanceRepository;

    @DisplayName("save() - entity is saved correctly")
    @Test
    void save_withoutError() {
        int roomId = prepareSampleGameRoomAndParticipation();

        GameRoom gameRoom = this.gameRoomRepository.findById(roomId).orElseThrow();

        log.info("gameRoomId={}", gameRoom.getRoomId());

        GameInstance gameInstance = new GameInstance();
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        gameInstance.setGameRoom(gameRoom);

        assertThatNoException().isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    @DisplayName("save() - roomId is null - DataIntegrityViolationException is expected")
    @Test
    void save_withError_roomIdIsNull() {
        prepareSampleGameRoomAndParticipation();

        GameInstance gameInstance = new GameInstance();
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    private int prepareSampleGameRoomAndParticipation() {
        GameRoom sampleGameRoom = new GameRoom();
        sampleGameRoom.setRoomName("sample-room");
        sampleGameRoom.setHostMemberId("sample-host");
        sampleGameRoom.setCreationTime(LocalDateTime.now());

        this.gameRoomRepository.save(sampleGameRoom);

        Participation sampleParticipation = new Participation(
                "sample-host",
                sampleGameRoom
        );
        this.participationRepository.save(sampleParticipation);

        return sampleGameRoom.getRoomId();
    }

    @DisplayName("save() - round is zero - JpaSystemException is expected")
    @Test
    void save_DataIntegrityException_roundIsZero() {
        int gameRoomId = prepareSampleGameRoomAndParticipation();

        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId).orElseThrow();

        GameInstance gameInstance = new GameInstance();
        // gameInstance.round is going to be set by 0
        // gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        gameInstance.setGameRoom(gameRoom);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    @DisplayName("save() - startTime is null - DataIntegrityException is expected")
    @Test
    void save_DataIntegrityException_startTimeIsNull() {
        int gameRoomId = prepareSampleGameRoomAndParticipation();

        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId).orElseThrow();

        GameInstance gameInstance = new GameInstance();
        gameInstance.setRound(1);
        gameInstance.setGamePhase(GamePhase.START);
        gameInstance.setGameRoom(gameRoom);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    @DisplayName("save() - DataIntegrityException - gamePhase is null")
    @Test
    void save_DataIntegrityException_gamePhaseIsNull() {
        int gameRoomId = prepareSampleGameRoomAndParticipation();

        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId).orElseThrow();

        GameInstance gameInstance = new GameInstance();
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGameRoom(gameRoom);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    @DisplayName("findById() - run correctly")
    @Test
    void findById() {
        int generatedId = insertSample();
        Optional<GameInstance> findGameInstanceOp = this.gameInstanceRepository.findById(generatedId);
        assertThat(findGameInstanceOp.isPresent()).isTrue();
    }

    @DisplayName("findById() - query data not exists")
    @Test
    void findById_queryForAbsentId() {
        int generatedId = insertSample();

        Optional<GameInstance> expectedNull = this.gameInstanceRepository.findById(generatedId + 1);
        assertThat(expectedNull.isEmpty()).isTrue();
    }

    @DisplayName("findAliveGameInstanceByGameRoomId() - succeed to find element")
    @Test
    void findAliveGameInstanceByGameRoomId() {
        int generatedId = insertSample();

        Integer roomId = this.gameInstanceRepository.findById(generatedId)
                .orElseThrow()
                .getGameRoom()
                .getRoomId();

        Optional<GameInstance> gameInstanceOp = this.gameInstanceRepository.findAliveGameInstanceByGameRoomId(roomId);
        assertThat(gameInstanceOp.isPresent()).isTrue();
        log.info("gameInstance={}", gameInstanceOp.get());
    }

    @DisplayName("findAliveGameInstanceByGameRoomId() - no result because the gameInstance has ended; i.e. endTime is not null")
    @Test
    void findAliveGameInstanceByGameRoomId_noResult_sinceItHasEnded() {
        int gameRoomId = prepareSampleGameRoomAndParticipation();

        GameInstance gameInstance = new GameInstance();
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setEndTime(LocalDateTime.now().plusHours(1));
        gameInstance.setGamePhase(GamePhase.START);
        gameInstance.setGameRoom(this.gameRoomRepository.findById(gameRoomId).orElseThrow());

        this.gameInstanceRepository.save(gameInstance);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() ->
                        this.gameInstanceRepository.findAliveGameInstanceByGameRoomId(gameRoomId)
                                .orElseThrow(NoSuchElementException::new));
    }

    @DisplayName("updateByGameRoomId()")
    @Test
    void updateByGameRoomId() {
        int generatedId = insertSample();

        GameInstance gameInstance = this.gameInstanceRepository.findById(generatedId).orElseThrow();
        int gameRoomId = gameInstance.getGameRoom().getRoomId();

        LocalDateTime offset = LocalDateTime.now();

        GameInstanceUpdateDto dto = GameInstanceUpdateDto.builder()
                .gameRoomId(gameRoomId)
                .round(2)
                .startTime(offset.minusDays(1))
                .endTime(offset.plusHours(2))
                .gamePhase(GamePhase.DAYTIME)
                .build();
        boolean result = this.gameInstanceRepository.updateByGameRoomId(dto);
        assertThat(result).isTrue();

        GameInstance updated = this.gameInstanceRepository.findById(generatedId).orElseThrow();
        log.info("updated={}", updated);
        assertThat(updated.getRound()).isEqualTo(2);
        assertThat(updated.getStartTime()).isEqualTo(offset.minusDays(1));
        assertThat(updated.getEndTime()).isEqualTo(offset.plusHours(2));
        assertThat(updated.getGamePhase()).isEqualTo(GamePhase.DAYTIME);
    }

    @DisplayName("updateByGameRoomId() - failed because gameInstance of gameRoomId isn't found")
    @Test
    void updateByGameRoomId_noGameInstanceWithGameRoomId() {
        insertSample();

        LocalDateTime offset = LocalDateTime.now();

        GameInstanceUpdateDto dto = GameInstanceUpdateDto.builder()
                .gameRoomId(0)
                .round(2)
                .startTime(offset.minusDays(1))
                .endTime(offset.plusHours(2))
                .gamePhase(GamePhase.DAYTIME)
                .build();
        boolean result = this.gameInstanceRepository.updateByGameRoomId(dto);
        assertThat(result).isFalse();
    }

    private int insertSample() {
        int gameRoomId = prepareSampleGameRoomAndParticipation();

        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId).orElseThrow();

        GameInstance gameInstance = new GameInstance();
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        gameInstance.setGameRoom(gameRoom);

        this.gameInstanceRepository.save(gameInstance);

        return gameInstance.getGameInstanceId();
    }
}
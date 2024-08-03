package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameroom.domain.GameRoom;
import nsl.webmapia.game.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class TestInMemoryGameInstanceRepository {

    InMemoryGameInstanceRepository gameInstanceRepository = new InMemoryGameInstanceRepository();

    @BeforeEach
    void beforeEach() {
        this.gameInstanceRepository.clear();
    }

    @DisplayName("Test save() method. Expected that the data is saved without any problem")
    @Test
    void save_noProblem() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomId(10000);
        gameRoom.setRoomName("sample-room");
        Member sampleHost = new Member("sample-member", "sample-nickname");
        gameRoom.setHostMember(sampleHost);
        gameRoom.setCreationTime(LocalDateTime.now());
        gameRoom.setParticipants(List.of(sampleHost));

        GameInstance gameInstance = new GameInstance();
        gameInstance.setGameRoom(gameRoom);
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        assertThatNoException().isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    @DisplayName("Test save() method. IllegalArgumentException is expected since GameRoom is null")
    @Test
    void save_IllegalArgumentException_sinceGameRoomIsNull() {
        GameInstance gameInstance = new GameInstance();
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    @DisplayName("Test save() method. IllegalArgumentException is expected since GameRoom.roomId is null")
    @Test
    void save_IllegalArgumentException_sinceRoomIdIsNull() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomName("sample-room");
        Member sampleHost = new Member("sample-member", "sample-nickname");
        gameRoom.setHostMember(sampleHost);
        gameRoom.setCreationTime(LocalDateTime.now());
        gameRoom.setParticipants(List.of(sampleHost));

        GameInstance gameInstance = new GameInstance();
        gameInstance.setGameRoom(gameRoom);
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> this.gameInstanceRepository.save(gameInstance));
    }

    @DisplayName("Test findById()")
    @Test
    void findById() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomId(10000);
        gameRoom.setRoomName("sample-room");
        Member sampleHost = new Member("sample-member", "sample-nickname");
        gameRoom.setHostMember(sampleHost);
        gameRoom.setCreationTime(LocalDateTime.now());
        gameRoom.setParticipants(List.of(sampleHost));

        GameInstance gameInstance = new GameInstance();
        gameInstance.setGameRoom(gameRoom);
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);

        this.gameInstanceRepository.save(gameInstance);

        Optional<GameInstance> result = this.gameInstanceRepository.findById(10000);
        assertThat(result.isPresent()).isTrue();

        GameInstance findInstance = result.get();

        assertThat(findInstance.getGameRoom().getRoomName()).isEqualTo("sample-room");
        assertThat(findInstance.getRound()).isEqualTo(1);
        assertThat(findInstance.getGamePhase()).isEqualTo(GamePhase.START);
    }
}
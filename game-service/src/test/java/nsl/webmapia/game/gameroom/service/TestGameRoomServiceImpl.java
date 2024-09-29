package nsl.webmapia.game.gameroom.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.InMemoryGameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class TestGameRoomServiceImpl {

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    GameRoomServiceImpl gameRoomServiceImpl;

    @Autowired
    GameInstanceRepository gameInstanceRepository;

    @BeforeEach
    void init() {
        if (this.gameRoomRepository instanceof InMemoryGameRoomRepository inMemoryGameRoomRepository) {
            inMemoryGameRoomRepository.clear();
        }
        this.gameRoomServiceImpl = new GameRoomServiceImpl(this.gameInstanceRepository,
                this.gameRoomRepository, this.participationRepository);
    }

    @DisplayName("createRoom() - test concurrently")
    @Test
    void createRoom() {
        for (int i = 0; i < 1000; i++) {
            String sampleHostId = "sample-member-" + i;
            String sampleRoomName = "sample-room-" + i;
            GameRoomCreationResponseDto dto = this.gameRoomServiceImpl.createRoom(sampleRoomName, sampleHostId);

            log.info("result={}", dto);

            assertThat(dto.getRoomId()).isNotZero();
            assertThat(dto.getRoomName()).isEqualTo(sampleRoomName);
            assertThat(dto.getHostMemberId()).isEqualTo(sampleHostId);
        }
    }

    @DisplayName("getGameRoom() - test concurrently")
    @Test
    void getGameRoom_withConcurrency() {
        Map<Integer, GameRoomCreationResponseDto> indexAndGameRoomCreationResponseDtoMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            String sampleHostId = "sample-member-" + i;
            String sampleRoomName = "sample-room-" + i;
            GameRoomCreationResponseDto dto = this.gameRoomServiceImpl.createRoom(sampleRoomName, sampleHostId);
            indexAndGameRoomCreationResponseDtoMap.put(i, dto);
        }

        for (final Integer idx : indexAndGameRoomCreationResponseDtoMap.keySet()) {
            GameRoomCreationResponseDto creationInfo = indexAndGameRoomCreationResponseDtoMap.get(idx);
            GameRoomDto resultDto = this.gameRoomServiceImpl.getGameRoom(creationInfo.getRoomId());

            assertThat(resultDto.getRoomId()).isEqualTo(creationInfo.getRoomId());
            assertThat(resultDto.getRoomName()).isEqualTo(creationInfo.getRoomName());
            assertThat(resultDto.getHostMemberId()).isEqualTo(creationInfo.getHostMemberId());
            assertThat(resultDto.getCreationTime()).isEqualTo(creationInfo.getCreationTime());
            assertThat(resultDto.getParticipantIds()).hasSize(1);
            assertThat(resultDto.getParticipantIds().get(0)).isEqualTo("sample-member-" + idx);
        }
    }
}
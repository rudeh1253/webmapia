package nsl.webmapia.game.gameroom.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.service.GameService;
import nsl.webmapia.game.gameroom.dto.ParticipationDto;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.exception.UnableToEnterGameRoomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class TestParticipationService {

    @Autowired
    ParticipationService participationService;

    @Autowired
    GameService gameService;

    @Autowired
    GameRoomService gameRoomService;

    @DisplayName("participate - successfully entered a GameRoom")
    @Test
    void participate_enter_succeed() {
        GameRoomCreationResponseDto dto = this.gameRoomService.createRoom("sample-room", "sample-creator");
        ParticipationDto participateDto = this.participationService.participate(dto.getRoomId(), "new-participant");
        log.info("result={}", participateDto);
        assertThat(participateDto.getNewParticipant()).isEqualTo("new-participant");
        assertThat(participateDto.getParticipants()).containsExactlyInAnyOrder("sample-creator", "new-participant");
    }

    @DisplayName("participate - failed to enter a GameRoom since already game started")
    @Test
    void participate_enter_fail_because_alreadyGameStarted() {
        GameRoomCreationResponseDto dto = this.gameRoomService.createRoom("sample-room", "sample-creator");
        this.gameService.startGame(dto.getRoomId());
        assertThatExceptionOfType(UnableToEnterGameRoomException.class)
                .isThrownBy(() -> this.participationService.participate(dto.getRoomId(), "new-participant"));
    }
}
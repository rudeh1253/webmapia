package nsl.webmapia.game.gameroom.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.service.GameService;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.dto.response.ParticipationResponseDto;
import nsl.webmapia.game.gameroom.exception.UnableToEnterGameRoomException;
import nsl.webmapia.game.member.dto.MemberDto;
import nsl.webmapia.game.member.entity.Member;
import nsl.webmapia.game.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
@SpringBootTest
@Transactional
class TestParticipationService {

    @Autowired
    ParticipationService participationService;

    @Autowired
    GameService gameService;

    @Autowired
    GameRoomService gameRoomService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("participate - successfully entered a GameRoom")
    @Test
    void participate_enter_succeed() {
        this.memberRepository.save(new Member("sample-creator", "nick"));
        this.memberRepository.save(new Member("new-participant", "nnn"));
        GameRoomCreationResponseDto dto = this.gameRoomService.createRoom("sample-room", "sample-creator");
        ParticipationResponseDto participateDto = this.participationService.participate(dto.getRoomId(), "new-participant");
        log.info("result={}", participateDto);
        assertThat(participateDto.getNewParticipant().getMemberId()).isEqualTo("new-participant");
        assertThat(participateDto.getParticipants().stream().map(MemberDto::getMemberId))
                .containsExactlyInAnyOrder("sample-creator", "new-participant");
    }

    @DisplayName("participate - failed to enter a GameRoom since already game started")
    @Test
    void participate_enter_fail_because_alreadyGameStarted() {
        this.memberRepository.save(new Member("sample-creator", "nick"));
        Member newParticipant = this.memberRepository.save(new Member("new-participant", "part"));
        GameRoomCreationResponseDto dto = this.gameRoomService.createRoom("sample-room", "sample-creator");
        this.gameService.startGame(dto.getRoomId());
        assertThatExceptionOfType(UnableToEnterGameRoomException.class)
                .isThrownBy(() -> this.participationService.participate(dto.getRoomId(), newParticipant.getMemberId()));
    }
}
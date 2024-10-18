package nsl.webmapia.game.domain.gameroom.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.domain.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.domain.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.domain.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.domain.gameroom.repository.InMemoryGameRoomRepository;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class TestGameRoomServiceImpl {

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    ParticipationService participationService;

    @Autowired
    GameRoomServiceImpl gameRoomServiceImpl;

    @Autowired
    GameInstanceRepository gameInstanceRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        if (this.gameRoomRepository instanceof InMemoryGameRoomRepository inMemoryGameRoomRepository) {
            inMemoryGameRoomRepository.clear();
        }
        this.gameRoomServiceImpl = new GameRoomServiceImpl(this.gameInstanceRepository,
                this.gameRoomRepository, this.participationService);
    }

    @DisplayName("createRoom() - test concurrently")
    @Test
    void createRoom() {
        for (int i = 0; i < 1000; i++) {
            String sampleHostId = "sample-member-" + i;
            this.memberRepository.save(new Member(sampleHostId, "nick" + i));
            String sampleRoomName = "sample-room-" + i;
            GameRoomCreationResponseDto dto = this.gameRoomServiceImpl.createRoom(sampleRoomName, sampleHostId);

            log.info("result={}", dto);

            assertThat(dto.getRoomId()).isNotZero();
            assertThat(dto.getRoomName()).isEqualTo(sampleRoomName);
            assertThat(dto.getHostMemberId()).isEqualTo(sampleHostId);
        }
    }

    @DisplayName("getGameRoom() - success")
    @Test
    void getGameRoom_success() {
        Member member = this.memberRepository.save(new Member("member", "nick"));
        GameRoomCreationResponseDto creationResponse = this.gameRoomServiceImpl.createRoom("sample-room", member.getMemberId());

        GameRoomDto result = this.gameRoomServiceImpl.getGameRoom(creationResponse.getRoomId());

        assertThat(result.getRoomName()).isEqualTo("sample-room");
        assertThat(result.getParticipants()).size().isEqualTo(1);
        assertThat(result.getParticipants().get(0).getParticipant().getMemberId()).isEqualTo("member");
    }
}
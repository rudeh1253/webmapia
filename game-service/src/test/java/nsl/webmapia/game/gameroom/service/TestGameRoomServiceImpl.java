package nsl.webmapia.game.gameroom.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.repository.InMemoryGameRoomRepository;
import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.member.dto.MemberDto;
import nsl.webmapia.game.member.service.MemberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TestGameRoomServiceImpl {
    InMemoryGameRoomRepository gameRoomRepository;
    static MemberService memberService = Mockito.mock(MemberService.class);
    GameRoomService gameRoomService;

    @BeforeAll
    static void globalInit() {
        for (int i = 0; i < 1000; i++) {
            String sampleId = "sample-member-" + i;
            String sampleNickname = "sample-nickname-" + i;
            Mockito.when(memberService.findMemberById(sampleId))
                    .thenReturn(MemberDto.of(new Member(sampleId, sampleNickname)));
        }
    }

    @BeforeEach
    void init() {
        if (this.gameRoomRepository == null) {
            this.gameRoomRepository = new InMemoryGameRoomRepository();
        } else {
            this.gameRoomRepository.clear();
        }
        this.gameRoomService = new GameRoomServiceImpl(this.gameRoomRepository, this.memberService);
    }

    @DisplayName("createRoom() - test concurrently")
    @Test
    void createRoom() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 1000; i++) {
            int id = i;
            executorService.submit(() -> {
                String sampleHostId = "sample-member-" + id;
                String sampleRoomName = "sample-room-" + id;
                GameRoomCreationResponseDto dto = this.gameRoomService.createRoom(sampleRoomName, sampleHostId);

                log.info("result={}", dto);

                assertThat(dto.getRoomId()).isNotZero();
                assertThat(dto.getRoomName()).isEqualTo(sampleRoomName);
                assertThat(dto.getHostMemberId()).isEqualTo(sampleHostId);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
    }

    @DisplayName("getGameRoom() - test concurrently")
    @Test
    void getGameRoom_withConcurrency() throws InterruptedException {
        Map<Integer, GameRoomCreationResponseDto> indexAndGameRoomCreationResponseDtoMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 1000; i++) {
            String sampleHostId = "sample-member-" + i;
            String sampleRoomName = "sample-room-" + i;
            GameRoomCreationResponseDto dto = this.gameRoomService.createRoom(sampleRoomName, sampleHostId);
            indexAndGameRoomCreationResponseDtoMap.put(i, dto);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (final Integer idx : indexAndGameRoomCreationResponseDtoMap.keySet()) {
            executorService.submit(() -> {
                GameRoomCreationResponseDto creationInfo = indexAndGameRoomCreationResponseDtoMap.get(idx);
                GameRoomDto resultDto = this.gameRoomService.getGameRoom(creationInfo.getRoomId());

                assertThat(resultDto.getRoomId()).isEqualTo(creationInfo.getRoomId());
                assertThat(resultDto.getRoomName()).isEqualTo(creationInfo.getRoomName());
                assertThat(resultDto.getMemberId()).isEqualTo(creationInfo.getHostMemberId());
                assertThat(resultDto.getCreationTime()).isEqualTo(creationInfo.getCreationTime());
                assertThat(resultDto.getParticipants().toArray(new MemberDto[0])).hasSize(1);
                assertThat(resultDto.getParticipants().get(0).getMemberId()).isEqualTo("sample-member-" + idx);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
    }
}
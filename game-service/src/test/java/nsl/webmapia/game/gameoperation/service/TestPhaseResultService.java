package nsl.webmapia.game.gameoperation.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;
import nsl.webmapia.game.gameoperation.repository.PhaseEndRequestRepository;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.gameroom.service.GameRoomService;
import nsl.webmapia.game.member.entity.Member;
import nsl.webmapia.game.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class TestPhaseResultService {
    static final String[] PARTICIPANT_IDS = {
            "member1",
            "member2",
            "member3",
            "member4",
            "member5",
            "member6",
            "member7",
            "member8",
            "member9",
            "member10",
            "member11",
            "member12",
            "member13",
    };

    @Autowired
    PhaseResultService phaseResultService;

    @Autowired
    PhaseEndRequestRepository phaseEndRequestRepository;

    @Autowired
    GameRoomService gameRoomService;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    GameService gameService;

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    MemberRepository memberRepository;

    int gameInstanceId;

    @BeforeEach
    void beforeEach() {
        List<Member> participants = Arrays.stream(PARTICIPANT_IDS)
                .map((id) -> new Member(id, id + "nick"))
                .map(this.memberRepository::save)
                .toList();

        GameRoomCreationResponseDto roomCreationDto =
                this.gameRoomService.createRoom("sample-room", participants.get(0).getMemberId());

        GameRoom gameRoom = this.gameRoomRepository.findById(roomCreationDto.getRoomId()).get();
        for (Member participant : participants) {
            if (participant.getMemberId().equals(participants.get(0).getMemberId())) {
                continue;
            }
            Participation participation = new Participation(participant, gameRoom);
            this.participationRepository.save(participation);
        }

        this.gameInstanceId = this.gameService.startGame(roomCreationDto.getRoomId());
        this.gameService.proceedPhase(this.gameInstanceId);
    }

    @DisplayName("endPhase - only a part of participants requested to end such that the phase won't end")
    @ValueSource(ints = {
            0, 1, 2, 3, 5, 12
    })
    @ParameterizedTest
    void endPhase_notEnded(int numOfRequesters) {
        Set<Integer> usageBucket = new HashSet<>();
        for (int i = 0; i < numOfRequesters; i++) {
            int randomIdx = getRandomWithoutDuplicate(PARTICIPANT_IDS.length, usageBucket);
            PhaseResultResponseDto result = this.phaseResultService.endPhase(this.gameInstanceId, PARTICIPANT_IDS[randomIdx]);
            log.info("PARTICIPANTS[randomIdx]={}", PARTICIPANT_IDS[randomIdx]);
            assertThat(result.isEnded()).isFalse();
            assertThat(result.getNextPhase()).isNull();
            assertThat(result.getCurrentPhase()).isNull();
            assertThat(result.getContent()).isNull();
        }
    }

    private int getRandomWithoutDuplicate(int range, Set<Integer> bucket) {
        Random random = new Random();
        int generated = random.nextInt(range);
        if (bucket.contains(generated)) {
            return getRandomWithoutDuplicate(range, bucket);
        }
        bucket.add(generated);
        return generated;
    }

    @DisplayName("endPhase - duplicate request of the same requester is expected to be ignored")
    @Test
    void endPhase_duplicateRequestFromTheSameRequester() {
        for (int i = 0; i < PARTICIPANT_IDS.length; i++) {
            PhaseResultResponseDto result = this.phaseResultService.endPhase(this.gameInstanceId, PARTICIPANT_IDS[0]);
            assertThat(result.isEnded()).isFalse();
            assertThat(result.getContent()).isNull();
            assertThat(result.getNextPhase()).isNull();
            assertThat(result.getCurrentPhase()).isNull();
        }

        Set<String> phaseEndRequesters = this.phaseEndRequestRepository.findPhaseEndRequestsByGameInstanceId(this.gameInstanceId);
        log.info("phaseEndRequesters={}", phaseEndRequesters);
        assertThat(phaseEndRequesters).containsExactly(PARTICIPANT_IDS[0]);
    }

    @DisplayName("endPhase - end is true")
    @Test
    void endPhase_ended() {
        for (int i = 0; i < PARTICIPANT_IDS.length - 1; i++) {
            PhaseResultResponseDto result = this.phaseResultService.endPhase(this.gameInstanceId, PARTICIPANT_IDS[i]);
            assertThat(result.isEnded()).isFalse();
            assertThat(result.getContent()).isNull();
            assertThat(result.getNextPhase()).isNull();
            assertThat(result.getCurrentPhase()).isNull();
        }
        PhaseResultResponseDto result = this.phaseResultService.endPhase(this.gameInstanceId, PARTICIPANT_IDS[PARTICIPANT_IDS.length - 1]);
        log.info("result={}", result);
        assertThat(result.isEnded()).isTrue();
        assertThat(result.getCurrentPhase()).isEqualTo(GamePhase.NIGHT);
        assertThat(result.getNextPhase()).isEqualTo(GamePhase.DISCUSSION);
    }
}
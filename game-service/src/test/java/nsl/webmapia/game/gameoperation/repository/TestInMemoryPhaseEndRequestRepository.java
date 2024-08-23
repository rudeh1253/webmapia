package nsl.webmapia.game.gameoperation.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.service.GameService;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.gameroom.repository.SpringDataJpaGameRoomRepository;
import nsl.webmapia.game.gameroom.service.GameRoomService;
import nsl.webmapia.game.gameroom.service.GameRoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@Slf4j
@SpringBootTest
@Transactional
class TestInMemoryPhaseEndRequestRepository {
    static final String[] PARTICIPANTS = {
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

    InMemoryPhaseEndRequestRepository inMemoryPhaseEndRequestRepository = new InMemoryPhaseEndRequestRepository();
    ExecutorService es;
    GameRoomService gameRoomService;

    @Autowired
    SpringDataJpaGameRoomRepository gameRoomRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    GameService gameService;

    Integer gameInstanceId;

    @BeforeEach
    void beforeEach() {
        inMemoryPhaseEndRequestRepository.clear();
        es = Executors.newCachedThreadPool();
        int createdRoomId = initGameRoom();
        participate(createdRoomId);
        initGameInstance(createdRoomId);
    }

    int initGameRoom() {
        this.gameRoomService = new GameRoomServiceImpl(
                this.gameRoomRepository,
                this.participationRepository
        );
        GameRoomCreationResponseDto dto = this.gameRoomService.createRoom("sample-room", "member1");
        return dto.getRoomId();
    }

    void participate(int gameRoomId) {
        GameRoom gameRoom = this.gameRoomRepository.findById(gameRoomId).get();
        for (String memberId : PARTICIPANTS) {
            if (memberId.equals("member1")) {
                continue;
            }
            Participation participation = new Participation(memberId, gameRoom);
            this.participationRepository.save(participation);
        }
    }

    void initGameInstance(int gameRoomId) {
        this.gameInstanceId = this.gameService.startGame(gameRoomId);
    }

    @DisplayName("savePhaseEndRequest - concurrently save phaseEndRequest")
    @Test
    void savePhaseEndRequest_concurrently() throws InterruptedException {
        for (String memberId : PARTICIPANTS) {
            es.submit(() -> assertThatNoException()
                    .isThrownBy(() -> this.inMemoryPhaseEndRequestRepository.savePhaseEndRequest(this.gameInstanceId, memberId)));
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.MINUTES);
    }

    @DisplayName("savePhaseEndRequest - save twice")
    @Test
    void savePhaseEndRequest_twice() {
        this.inMemoryPhaseEndRequestRepository.savePhaseEndRequest(gameInstanceId, "member1");
        assertThatNoException()
                .isThrownBy(() -> this.inMemoryPhaseEndRequestRepository.savePhaseEndRequest(gameInstanceId, "member1"));
    }

    @DisplayName("savePhaseEndObjective - concurrently")
    @Test
    void savePhaseEndObjective_concurrently() throws InterruptedException {
        List<Participation> participations =
                this.participationRepository.findNotDisconnectedByGameInstanceId(this.gameInstanceId);
        for (int i = 0; i < 100; i++) {
            es.submit(() -> assertThatNoException()
                    .isThrownBy(() -> this.inMemoryPhaseEndRequestRepository.savePhaseEndObjective(this.gameInstanceId, participations.size())));
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.MINUTES);
    }

    @DisplayName("findPhaseEndRequestsByGameInstanceId - check if ")
    @Test
    void findPhaseEndRequestsByGameInstanceId() throws InterruptedException {
        for (String memberId : PARTICIPANTS) {
            es.submit(() -> {
                assertThatNoException()
                        .isThrownBy(() -> this.inMemoryPhaseEndRequestRepository.savePhaseEndRequest(this.gameInstanceId, memberId));
            });
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.MINUTES);

        Set<String> phaseEndRequesters =
                this.inMemoryPhaseEndRequestRepository.findPhaseEndRequestsByGameInstanceId(this.gameInstanceId);
        phaseEndRequesters.forEach((p) -> log.info("p={}", p));

        assertThat(phaseEndRequesters).containsExactlyInAnyOrder(PARTICIPANTS);
    }

    @Test
    void findPhaseEndObjectiveByGameInstanceId() {
        Optional<Integer> objBefore = this.inMemoryPhaseEndRequestRepository.findPhaseEndObjectiveByGameInstanceId(this.gameInstanceId);
        assertThat(objBefore).isEmpty();

        this.inMemoryPhaseEndRequestRepository.savePhaseEndObjective(this.gameInstanceId, 10);
        Optional<Integer> objAfter = this.inMemoryPhaseEndRequestRepository.findPhaseEndObjectiveByGameInstanceId(this.gameInstanceId);
        assertThat(objAfter.get()).isEqualTo(10);
    }
}
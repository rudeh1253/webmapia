package nsl.webmapia.game.gameoperation.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.domain.Vote;
import nsl.webmapia.game.gameroom.domain.GameRoom;
import nsl.webmapia.game.gameroom.repository.SpringDataJpaGameRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class TestSpringDataJpaVoteRepository {

    @Autowired
    SpringDataJpaGameRoomRepository gameRoomRepository;

    @Autowired
    HibernateGameInstanceRepository gameInstanceRepository;

    @Autowired
    SpringDataJpaVoteRepository voteRepository;

    @DisplayName("Check if save() works without any exception")
    @Test
    void save() {
        GameInstance gameInstance = insertSampleGameRoomAndGameInstance();

        Vote vote = new Vote(
                1,
                "sample-source",
                "sample-target",
                1,
                gameInstance
        );
        assertThatNoException().isThrownBy(() -> this.voteRepository.save(vote));
    }

    @DisplayName("findByGameInstanceAndRound() - find correctly")
    @Test
    void findByGameInstanceAndRound() {
        GameInstance gameInstance = insertSampleGameRoomAndGameInstance();

        Vote vote1 = new Vote(
                1,
                "sample-source1",
                "sample-target",
                1,
                gameInstance
        );
        this.voteRepository.save(vote1);

        Vote vote2 = new Vote(
                1,
                "sample-source2",
                "sample-target",
                1,
                gameInstance
        );
        this.voteRepository.save(vote2);

        Set<Vote> result = this.voteRepository.findByGameInstanceAndRound(gameInstance, 1);

        result.forEach((v) -> log.info("v={}", v));

        assertThat(result.stream().map(Vote::getVoteId).toList())
                .containsExactlyInAnyOrder(vote1.getVoteId(), vote2.getVoteId());
    }

    private GameInstance insertSampleGameRoomAndGameInstance() {

        GameRoom gameRoom = new GameRoom(
                "sample-room",
                "sample-host",
                LocalDateTime.now()
        );
        this.gameRoomRepository.save(gameRoom);

        GameInstance gameInstance = GameInstance.builder()
                .round(1)
                .startTime(LocalDateTime.now())
                .gamePhase(GamePhase.START)
                .gameRoom(gameRoom)
                .build();
        this.gameInstanceRepository.save(gameInstance);
        return gameInstance;
    }
}
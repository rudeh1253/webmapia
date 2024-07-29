package nsl.webmapia.game.gameroom.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.gameroom.domain.GameRoom;
import nsl.webmapia.game.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
class TestInMemoryGameRoomRepository {
    InMemoryGameRoomRepository inMemoryGameRoomRepository = new InMemoryGameRoomRepository();

    @BeforeEach
    void init() {
        this.inMemoryGameRoomRepository.clear();
    }

    @DisplayName("Check concurrency of save() method")
    @Test
    void save_findById_concurrency() {
        final int SIZE_OF_TESTCASE = 10000;
        List<GameRoom> testCase = new LinkedList<>();
        for (int i = 0; i < SIZE_OF_TESTCASE; i++) {
            testCase.add(new GameRoom(
                    "sample-room" + i, new Member("sample-" + i, "nick-" + i), LocalDateTime.now(), List.of()
            ));
        }

        log.info("testCase.size()={}", testCase.size());
        ExecutorService executorService = Executors.newCachedThreadPool();
        testCase.forEach((each) -> {
            executorService.submit(() -> {
                int generatedId = this.inMemoryGameRoomRepository.save(each);
                log.info("generatedId={}", generatedId);
                Optional<GameRoom> found = this.inMemoryGameRoomRepository.findById(generatedId);
                assertThat(found.isPresent()).isTrue();
                log.info("found GameRoom instance={}", found.get());
                assertThat(found.get().getRoomName()).isEqualTo(each.getRoomName());
            });
        });
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                Optional<GameRoom> byId = this.inMemoryGameRoomRepository.findById(SIZE_OF_TESTCASE - 1 + 10000);
                assertThat(byId.isPresent()).isTrue();
                log.info("last element={}", byId.get());
                break;
            }
        }
    }

    @DisplayName("Check concurrent of update() method")
    @Test
    void update_concurrency() throws InterruptedException {
        List<GameRoom> testCase = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            testCase.add(new GameRoom(
                    "sample-room" + i, new Member("sample-" + i, "nick-" + i), LocalDateTime.now(), List.of()
            ));
        }

        log.info("testCase.size()={}", testCase.size());

        for (GameRoom unitCase : testCase) {
            this.inMemoryGameRoomRepository.save(unitCase);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        Object lock = new Object();
        for (int i = 13000; i < 15000; i++) {
            final int id = i;
            executorService.submit(() -> {
                GameRoomUpdateDto dto = GameRoomUpdateDto.builder()
                        .roomId(id)
                        .roomName("updated-" + id)
                        .build();
                boolean updateSuccess = this.inMemoryGameRoomRepository.update(dto);
                log.info("Update completed - id={}", id);
                assertThat(updateSuccess).isTrue();
                synchronized (lock) {
                    lock.notify();
                }
            });
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            synchronized (lock) {
                lock.wait();
            }
        }
        for (int id = 13000; id < 15000; id++) {
            Optional<GameRoom> gameRoomOp = this.inMemoryGameRoomRepository.findById(id);
            assertThat(gameRoomOp.isPresent()).isTrue();
            log.info("found gameRoom={}", gameRoomOp.get());
            assertThat(gameRoomOp.get().getRoomName()).isEqualTo("updated-" + id);
        }
    }

    @DisplayName("Attempts to update without roomId - IllegalArgumentException is expected")
    @Test
    void update_illegalArgumentException() throws Exception {
        List<GameRoom> testCase = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            testCase.add(new GameRoom(
                    "sample-room" + i, new Member("sample-" + i, "nick-" + i), LocalDateTime.now(), List.of()
            ));
        }

        log.info("testCase.size()={}", testCase.size());

        for (GameRoom unitCase : testCase) {
            this.inMemoryGameRoomRepository.save(unitCase);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        Object lock = new Object();
        for (int i = 13000; i < 15000; i++) {
            final int id = i;
            executorService.submit(() -> {
                GameRoomUpdateDto dto = GameRoomUpdateDto.builder()
                        .roomName("updated-" + id)
                        .build();
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> this.inMemoryGameRoomRepository.update(dto));
                synchronized (lock) {
                    lock.notify();
                }
            });
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            synchronized (lock) {
                lock.wait();
            }
        }
        for (int id = 13000; id < 15000; id++) {
            Optional<GameRoom> gameRoomOp = this.inMemoryGameRoomRepository.findById(id);
            assertThat(gameRoomOp.isPresent()).isTrue();
            log.info("found gameRoom={}", gameRoomOp.get());
            assertThat(gameRoomOp.get().getRoomName()).isNotEqualTo("updated-" + id);
        }
    }
}
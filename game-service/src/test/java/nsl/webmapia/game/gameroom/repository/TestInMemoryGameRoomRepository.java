package nsl.webmapia.game.gameroom.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.common.NumberConstants;
import nsl.webmapia.game.common.dto.PageDto;
import nsl.webmapia.game.common.dto.PageWrapper;
import nsl.webmapia.game.gameroom.domain.GameRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

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
                    "sample-room" + i, "sample-" + i, LocalDateTime.now(), List.of()
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

    @DisplayName("findAll() - size=205/without passing pageSize")
    @ValueSource(ints = {
            0, 1, 2, 20, 21, 22, 100
    })
    @ParameterizedTest
    void findAll_defaultPageSize(int page) {
        final int TESTCASE_SIZE = 205; // TESTCASE_SIZE is flexible and open/closed

        List<LocalDateTime> sampleTimes = getSampleTimes(TESTCASE_SIZE, (s) -> true, (i) -> "sample-room-name-" + i);

        PageWrapper<GameRoom> result = this.inMemoryGameRoomRepository.findAll(new PageDto(page, null));

        testRangeQuery(page, NumberConstants.DEFAULT_QUERY_PAGE_SIZE.get(), sampleTimes, result);
    }

    @DisplayName("findAll() - size=205/with specific pageSize")
    @CsvSource(
            value = {
                    "1:15", "2:15", "2:15", "20:18", "20:20", "10:20", "1:10", "21:10",
                    "0:15", "0:12", "0:10"
            },
            delimiter = ':'
    )
    @ParameterizedTest
    void findAll_arbitraryPageSize(int page, int pageSize) {
        final int TESTCASE_SIZE = 205; // TESTCASE_SIZE is flexible and open/closed

        List<LocalDateTime> sampleTimes = getSampleTimes(TESTCASE_SIZE, (s) -> true, (i) -> "sample-room-name-" + i);

        PageWrapper<GameRoom> result = this.inMemoryGameRoomRepository.findAll(new PageDto(page, pageSize));

        testRangeQuery(page, pageSize, sampleTimes, result);
    }

    @DisplayName("findByRoomName() - size=212/with default page size")
    @ValueSource(ints = {
            1, 2, 5, 8, 10, 11, 12, 13, 15, 20, 21, 22, 25
    })
    @ParameterizedTest
    void findByRoomName(int page) {
        final int TESTCASE_SIZE = 212;

        List<LocalDateTime> sampleTimes =
                getSampleTimes(
                        TESTCASE_SIZE,
                        (s) -> s.startsWith("to-be-queried"),
                        (i) -> i % 4 < 2 ? "sample-room-name-" + i : "to-be-queried-" + i
                );

        log.info("sampleTime.size()={}", sampleTimes.size());

        PageWrapper<GameRoom> result = this.inMemoryGameRoomRepository.findByRoomName("to-be-queried", new PageDto(page, null));

        testRangeQuery(page, NumberConstants.DEFAULT_QUERY_PAGE_SIZE.get(), sampleTimes, result);
    }

    @DisplayName("findByRoomName() - size=212/with specific page size")
    @CsvSource(
            value = {
                    "1:15", "2:15", "2:15", "20:18", "20:20", "10:20", "1:10", "21:10",
                    "0:15", "0:10", "0:12"
            },
            delimiter = ':'
    )
    @ParameterizedTest
    void findByRoomName_specificPageSize(int page, int pageSize) {
        final int TESTCASE_SIZE = 212;

        List<LocalDateTime> sampleTimes =
                getSampleTimes(
                        TESTCASE_SIZE,
                        (s) -> s.startsWith("to-be-queried"),
                        (i) -> i % 4 < 2 ? "sample-room-name-" + i : "to-be-queried-" + i
                );

        log.info("sampleTime.size()={}", sampleTimes.size());

        PageWrapper<GameRoom> result = this.inMemoryGameRoomRepository.findByRoomName("to-be-queried", new PageDto(page, pageSize));

        testRangeQuery(page, pageSize, sampleTimes, result);
    }

    private List<LocalDateTime> getSampleTimes(int testcaseSize,
                                               Predicate<String> sampleTimesCondition,
                                               Function<Integer, String> roomNameGenerator) {
        List<LocalDateTime> timePool = new ArrayList<>();
        LocalDateTime offsetTime = LocalDateTime.of(2017, Month.APRIL, 10, 5, 45);
        for (int i = 0; i < testcaseSize; i++) {
            LocalDateTime time = offsetTime.plus(i, ChronoUnit.MINUTES);
            timePool.add(time);
        }
        Collections.shuffle(timePool);

        List<LocalDateTime> sampleTimes = new ArrayList<>();
        for (int i = 0; i < timePool.size(); i++) {
            LocalDateTime time = timePool.get(i);
            GameRoom sampleGameRoom = new GameRoom(
                    roomNameGenerator.apply(i),
                    "sample-member-" + i,
                    time,
                    List.of("sample-member-" + i)
            );
            this.inMemoryGameRoomRepository.save(sampleGameRoom);
            if (sampleTimesCondition.test(sampleGameRoom.getRoomName())) {
                sampleTimes.add(time);
            }
        }

        sampleTimes.sort((t1, t2) ->
                t1.isBefore(t2)
                        ? 1
                        : t1.isAfter(t2)
                        ? -1
                        : 0);
        return sampleTimes;
    }

    private void testRangeQuery(int page,
                                int pageSize,
                                List<LocalDateTime> sampleTimes,
                                PageWrapper<GameRoom> result) {
        page = Math.max(0, page);
        pageSize = Math.max(0, pageSize);
        int dataSize = sampleTimes.size();
        assertThat(result.getPage()).isEqualTo(page);
        assertThat(result.getTotalElementCount()).isEqualTo(dataSize);
        assertThat(result.getTotalPage()).isEqualTo(dataSize / pageSize + (dataSize % pageSize == 0 ? 0 : 1));

        List<LocalDateTime> expected = getSubListOf(sampleTimes, (page - 1) * pageSize, page * pageSize);
        assertThat(result.getElements().stream().map(GameRoom::getCreationTime).toList())
                .containsExactly(expected.toArray(new LocalDateTime[0]));
    }

    private <T> List<T> getSubListOf(List<T> list, int lowerBoundInclusive, int upperBoundExclusive) {
        if (lowerBoundInclusive < 0) {
            return new ArrayList<>();
        }
        int listSize = list.size();
        upperBoundExclusive = Math.min(upperBoundExclusive, listSize);
        if (lowerBoundInclusive < listSize) {
            return list.subList(lowerBoundInclusive, upperBoundExclusive);
        } else {
            return new ArrayList<>();
        }
    }

    @DisplayName("Check concurrency of update() method")
    @Test
    void update_concurrency() throws InterruptedException {
        List<GameRoom> testCase = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            testCase.add(new GameRoom(
                    "sample-room" + i, "sample-" + i, LocalDateTime.now(), List.of()
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
            });
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
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
                    "sample-room" + i, "sample-" + i, LocalDateTime.now(), List.of()
            ));
        }

        log.info("testCase.size()={}", testCase.size());

        for (GameRoom unitCase : testCase) {
            this.inMemoryGameRoomRepository.save(unitCase);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 13000; i < 15000; i++) {
            final int id = i;
            executorService.submit(() -> {
                GameRoomUpdateDto dto = GameRoomUpdateDto.builder()
                        .roomName("updated-" + id)
                        .build();
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> this.inMemoryGameRoomRepository.update(dto));
            });
        }

        executorService.shutdown();

        executorService.awaitTermination(1, TimeUnit.MINUTES);
        for (int id = 13000; id < 15000; id++) {
            Optional<GameRoom> gameRoomOp = this.inMemoryGameRoomRepository.findById(id);
            assertThat(gameRoomOp.isPresent()).isTrue();
            log.info("found gameRoom={}", gameRoomOp.get());
            assertThat(gameRoomOp.get().getRoomName()).isNotEqualTo("updated-" + id);
        }
    }
}
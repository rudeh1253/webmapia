package com.nsl.webmapia.room.service;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.gameoperation.repository.MemoryGameRepository;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.user.service.UserService;
import com.nsl.webmapia.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class RoomServiceImplTest {
    RoomService roomService;
    UserService userService;

    @BeforeEach
    public void initialize() {
        GameRepository gameRepository = new MemoryGameRepository();
        roomService = new RoomServiceImpl(gameRepository);
        userService = new UserServiceImpl(gameRepository);
    }

    @Test
    public void createNewGame() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            futures.add(executor.submit(() -> {
                Long gameId = roomService.createNewRoom("asdf", 1L);
                userService.addUser(gameId, 1L);
                return gameId;
            }));
        }
        List<Long> gameIds = new ArrayList<>();
        futures.forEach(e -> {
            try {
                gameIds.add(e.get());
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        executor.shutdown();
        while (!executor.isTerminated()) {}
        gameIds.forEach(id -> assertNotNull(roomService.getRoomInfo(id)));
    }
}
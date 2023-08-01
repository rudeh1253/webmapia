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
        SkillManager skillManager = new SkillManager();
        Wolf wolf = new Wolf(skillManager);
        Betrayer betrayer = new Betrayer(skillManager);
        Citizen citizen = new Citizen(skillManager);
        Detective detective = new Detective(skillManager);
        Follower follower = new Follower(skillManager);
        Guard guard = new Guard(skillManager);
        HumanMouse humanMouse = new HumanMouse(skillManager);
        Mediumship mediumship = new Mediumship(skillManager);
        Murderer murderer = new Murderer(skillManager);
        Nobility nobility = new Nobility(skillManager);
        Predictor predictor = new Predictor(skillManager);
        SecretSociety secretSociety = new SecretSociety(skillManager);
        Soldier soldier = new Soldier(skillManager);
        Templar templar = new Templar(skillManager);
        Successor successor = new Successor(skillManager);

        Characters characters = new Characters(wolf, betrayer, citizen, detective, follower, guard, humanMouse, mediumship,
                murderer, nobility, predictor, secretSociety, soldier, successor, templar);
        GameRepository gameRepository = new MemoryGameRepository();
        roomService = new RoomServiceImpl(characters, gameRepository);
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
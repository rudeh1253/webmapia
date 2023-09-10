package com.nsl.webmapia.user.service;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.user.dto.UserResponseDTO;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.gameoperation.repository.MemoryGameRepository;
import com.nsl.webmapia.room.dto.RoomInfoResponseDTO;
import com.nsl.webmapia.room.service.RoomService;
import com.nsl.webmapia.room.service.RoomServiceImpl;
import com.nsl.webmapia.skill.domain.SkillManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceImplTest {
    UserService userService;
    RoomService roomService;

    @BeforeEach
    public void initialize() {
        Wolf wolf = new Wolf();
        Betrayer betrayer = new Betrayer();
        Citizen citizen = new Citizen();
        Detective detective = new Detective();
        Follower follower = new Follower();
        Guard guard = new Guard();
        HumanMouse humanMouse = new HumanMouse();
        Mediumship mediumship = new Mediumship();
        Murderer murderer = new Murderer();
        Nobility nobility = new Nobility();
        Predictor predictor = new Predictor();
        SecretSociety secretSociety = new SecretSociety();
        Soldier soldier = new Soldier();
        Templar templar = new Templar();
        Successor successor = new Successor();
        Characters characters = new Characters(wolf, betrayer, citizen, detective, follower, guard, humanMouse, mediumship,
                murderer, nobility, predictor, secretSociety, soldier, successor, templar);
        GameRepository gameRepository = new MemoryGameRepository();
        roomService = new RoomServiceImpl(characters, gameRepository);
        userService = new UserServiceImpl(gameRepository);
    }

    @Test
    void addUser() {
        Long gameId = roomService.createNewRoom("asdf", 1L);
        userService.addUser(gameId, 1L);
        assertEquals(1, userService.getAllUsers(gameId).size());
    }

    @Test
    void removeUser() {
        Long gameId = roomService.createNewRoom("asdf", 1L);
        userService.addUser(gameId, 1L);
        userService.addUser(gameId, 2L);
        assertEquals(2, userService.getAllUsers(gameId).size());
        UserResponseDTO removeNotification = userService.removeUser(gameId, 1L);
        assertEquals(1, userService.getAllUsers(gameId).size());
        assertEquals(2L, userService.getAllUsers(gameId).get(0).getUserId());
        assertEquals(gameId, removeNotification.getGameId());
        assertEquals(NotificationType.USER_REMOVED, removeNotification.getNotificationType());
        assertEquals(1L, removeNotification.getUserId());
    }

    @Test
    public void addUsersTest() {
        List<Long> gameIds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            gameIds.add(roomService.createNewRoom("asdf", 1L));
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        gameIds.forEach(id -> executorService.submit(() -> addUsers(id, 16)));
        executorService.shutdown();
        try {
            boolean terminated = executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            assertTrue(terminated);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<RoomInfoResponseDTO> games = roomService.getAllRoomInfo();
        for (RoomInfoResponseDTO game : games) {
            assertTrue(gameIds.contains(game.getRoomId()));
        }
        for (RoomInfoResponseDTO game : games) {
            List<UserResponseDTO> users = game.getUsers();
            assertEquals(16, users.size());
        }
    }

    @Test
    void removeUser_concurrent() {
        List<Long> gameIds = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            gameIds.add(roomService.createNewRoom("asdf", 1L));
        }
        gameIds.forEach(id -> {
            userService.addUser(id, 1L);
            userService.addUser(id, 2L);
            assertEquals(2, userService.getAllUsers(id).size());
        });

        ExecutorService executor = Executors.newCachedThreadPool();
        Map<Long, UserResponseDTO> notifications = new ConcurrentHashMap<>();
        gameIds.forEach(id -> executor.submit(() -> notifications.put(id, userService.removeUser(id, 1L))));
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1000, notifications.size());
        for (Long key : notifications.keySet()) {
            assertEquals(1, userService.getAllUsers(key).size());
            assertEquals(2L, userService.getAllUsers(key).get(0).getUserId());
            assertEquals(NotificationType.USER_REMOVED, notifications.get(key).getNotificationType());
            assertEquals(1L, notifications.get(key).getUserId());
        }
    }

    void addUsers(Long gameId, int num) {
        for (int i = 0; i < num; i++) {
            userService.addUser(gameId, (long)(i + 1));
        }
    }
}
package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.NotificationBody;
import com.nsl.webmapia.game.domain.notification.NotificationType;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.repository.GameRepository;
import com.nsl.webmapia.game.repository.MemoryGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceImplConcurrencyTest {
    GameService gameService;

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

        gameService = new GameServiceImpl(wolf, betrayer, citizen, detective, follower, guard, humanMouse, mediumship,
                murderer, nobility, predictor, secretSociety, soldier, successor, templar, new MemoryGameRepository());
    }

    @Test
    public void createNewGame() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            futures.add(executor.submit(() -> gameService.createNewGame()));
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
        gameIds.forEach(id -> assertNotNull(gameService.getGame(id)));
    }

    void addUsers(Long gameId, int num) {
        for (int i = 0; i < num; i++) {
            gameService.addUser(gameId, (long)(i + 1));
        }
    }

    @Test
    public void addUsersTest() {
        List<Long> gameIds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            gameIds.add(gameService.createNewGame());
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
        List<GameManager> games = gameService.getAllGames();
        for (GameManager game : games) {
            assertTrue(gameIds.contains(game.getGameId()));
        }
        for (GameManager game : games) {
            List<User> users = game.getAllUsers();
            assertEquals(16, users.size());
        }
    }

    @Test
    void generateCharacters() {
        List<Long> gameIds = new LinkedList<>();
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        characterDistribution.put(CharacterCode.HUMAN_MOUSE, 1);
        characterDistribution.put(CharacterCode.MEDIUMSHIP, 1);
        characterDistribution.put(CharacterCode.MURDERER, 1);
        characterDistribution.put(CharacterCode.NOBILITY, 1);
        characterDistribution.put(CharacterCode.PREDICTOR, 1);
        characterDistribution.put(CharacterCode.SECRET_SOCIETY, 2);
        characterDistribution.put(CharacterCode.SOLDIER, 1);
        characterDistribution.put(CharacterCode.SUCCESSOR, 1);
        characterDistribution.put(CharacterCode.TEMPLAR, 1);
        for (int i = 0; i < 100; i++) {
            Long gameId = gameService.createNewGame();
            gameIds.add(gameId);
            addUsers(gameId, 16);
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        List<List<NotificationBody<Character>>> characterNotifications = Collections.synchronizedList(new LinkedList<>());
        gameIds.forEach(id -> executor.submit(() -> characterNotifications.add(gameService.generateCharacters(id, characterDistribution))));
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Long> userIds = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            userIds.add((long)i);
        }
        assertEquals(100, characterNotifications.size());

        for (List<NotificationBody<Character>> notificationList : characterNotifications) {
            assertEquals(16, notificationList.size());
            for (NotificationBody<Character> notification : notificationList) {
                assertEquals(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED, notification.getNotificationType());
                assertTrue(userIds.contains(notification.getReceiver().getID()));
                characterDistribution.keySet().forEach(e -> {
                    List<NotificationBody<Character>> filtered = notificationList.stream()
                            .filter(t -> t.getData().getCharacterCode() == e)
                            .toList();
                    assertEquals(characterDistribution.get(e), filtered.size());
                });
            }
        }
    }
//
//    @Test
//    void stepForward() {
//    }
//
//    @Test
//    void processVotes() {
//        addUsers(gameId, 5);
//        List<User> users = gameService.getAllUsers(gameId);
//        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
//        characterDistribution.put(CharacterCode.WOLF, 1);
//        characterDistribution.put(CharacterCode.BETRAYER, 1);
//        characterDistribution.put(CharacterCode.CITIZEN, 1);
//        characterDistribution.put(CharacterCode.DETECTIVE, 1);
//        characterDistribution.put(CharacterCode.FOLLOWER, 1);
//        gameService.generateCharacters(gameId, characterDistribution);
//        gameService.acceptVote(gameId, users.get(0).getID(), users.get(1).getID());
//        gameService.acceptVote(gameId, users.get(1).getID(), users.get(2).getID());
//        gameService.acceptVote(gameId, users.get(2).getID(), users.get(1).getID());
//        gameService.acceptVote(gameId, users.get(3).getID(), users.get(1).getID());
//        gameService.acceptVote(gameId, users.get(4).getID(), users.get(1).getID());
//        NotificationBody<User> voteResult = gameService.processVotes(gameId);
//        assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
//        assertEquals(users.get(1), voteResult.getData());
//    }
//
//    @Test
//    void processVotes_tie() {
//        addUsers(gameId, 6);
//        List<User> users = gameService.getAllUsers(gameId);
//        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
//        characterDistribution.put(CharacterCode.WOLF, 1);
//        characterDistribution.put(CharacterCode.BETRAYER, 1);
//        characterDistribution.put(CharacterCode.CITIZEN, 1);
//        characterDistribution.put(CharacterCode.DETECTIVE, 1);
//        characterDistribution.put(CharacterCode.FOLLOWER, 1);
//        characterDistribution.put(CharacterCode.GUARD, 1);
//        gameService.generateCharacters(gameId, characterDistribution);
//        gameService.acceptVote(gameId, users.get(0).getID(), users.get(1).getID());
//        gameService.acceptVote(gameId, users.get(1).getID(), users.get(2).getID());
//        gameService.acceptVote(gameId, users.get(2).getID(), users.get(2).getID());
//        gameService.acceptVote(gameId, users.get(3).getID(), users.get(2).getID());
//        gameService.acceptVote(gameId, users.get(4).getID(), users.get(1).getID());
//        gameService.acceptVote(gameId, users.get(5).getID(), users.get(1).getID());
//        NotificationBody<User> voteResult = gameService.processVotes(gameId);
//        assertEquals(NotificationType.INVALID_VOTE, voteResult.getNotificationType());
//        assertNull(voteResult.getData());
//    }
//
//    @Test
//    void processVote_includeNobility() {
//        addUsers(gameId, 6);
//        List<User> users = gameService.getAllUsers(gameId);
//        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
//        characterDistribution.put(CharacterCode.WOLF, 1);
//        characterDistribution.put(CharacterCode.BETRAYER, 1);
//        characterDistribution.put(CharacterCode.CITIZEN, 1);
//        characterDistribution.put(CharacterCode.DETECTIVE, 1);
//        characterDistribution.put(CharacterCode.FOLLOWER, 1);
//        characterDistribution.put(CharacterCode.NOBILITY, 1);
//        gameService.generateCharacters(gameId, characterDistribution);
//        gameService.acceptVote(gameId, users.get(0).getID(), users.get(1).getID());
//        gameService.acceptVote(gameId, users.get(1).getID(), users.get(2).getID());
//        gameService.acceptVote(gameId, users.get(2).getID(), users.get(2).getID());
//        gameService.acceptVote(gameId, users.get(3).getID(), users.get(2).getID());
//        gameService.acceptVote(gameId, users.get(4).getID(), users.get(1).getID());
//        gameService.acceptVote(gameId, users.get(5).getID(), users.get(1).getID());
//        NotificationBody<User> voteResult = gameService.processVotes(gameId);
//        assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
//        assertEquals(users.get(1), voteResult.getData());
//    }
//
//    @Test
//    void addUser() {
//        gameService.addUser(gameId, 1L);
//        assertEquals(1, gameService.getAllUsers(gameId).size());
//    }
//
//    @Test
//    void removeUser() {
//        gameService.addUser(gameId, 1L);
//        gameService.addUser(gameId, 2L);
//        assertEquals(2, gameService.getAllUsers(gameId).size());
//        NotificationBody<User> removeNotification = gameService.removeUser(gameId, 1L);
//        assertEquals(1, gameService.getAllUsers(gameId).size());
//        assertEquals(2L, gameService.getAllUsers(gameId).get(0).getID());
//        assertEquals(gameId, removeNotification.getGameId());
//        assertNull(removeNotification.getReceiver());
//        assertEquals(NotificationType.USER_REMOVED, removeNotification.getNotificationType());
//        assertEquals(1L, removeNotification.getData().getID());
//    }
}

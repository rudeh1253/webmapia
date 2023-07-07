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
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceImplConcurrencyTest {
    Long gameId;
    GameService gameService;
    UserRepository userRepository;
    GameRepository gameRepository;

    @BeforeEach
    public void initialize() {
        userRepository = new MemoryUserRepository();
        gameRepository = new MemoryGameRepository();
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
                murderer, nobility, predictor, secretSociety, soldier, successor, templar, gameRepository);
        gameId = gameService.createNewGame();
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

    void addUsers(int num) {
        for (int i = 0; i < num; i++) {
            gameService.addUser(gameId, (long)(i + 1));
        }
    }

    @Test
    void generateCharacters() {
        addUsers(16);
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
        List<NotificationBody<Character>> charNotifications = gameService.generateCharacters(gameId, characterDistribution);
        List<Long> userIds = new ArrayList<>(16);
        for (User u : gameService.getAllUsers(gameId)) {
            userIds.add(u.getID());
        }

        charNotifications.forEach(e -> {
            assertEquals(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED, e.getNotificationType());
            assertEquals(gameId, e.getGameId());
            assertTrue(userIds.contains(e.getReceiver().getID()));
        });

        characterDistribution.keySet().forEach(k -> {
            assertEquals(characterDistribution.get(k),
                    charNotifications.stream().filter(e -> e.getData().getCharacterCode() == k)
                            .toList().size());
        });
    }

    @Test
    void stepForward() {
    }

    @Test
    void processVotes() {
        addUsers(5);
        List<User> users = gameService.getAllUsers(gameId);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        gameService.generateCharacters(gameId, characterDistribution);
        gameService.acceptVote(gameId, users.get(0).getID(), users.get(1).getID());
        gameService.acceptVote(gameId, users.get(1).getID(), users.get(2).getID());
        gameService.acceptVote(gameId, users.get(2).getID(), users.get(1).getID());
        gameService.acceptVote(gameId, users.get(3).getID(), users.get(1).getID());
        gameService.acceptVote(gameId, users.get(4).getID(), users.get(1).getID());
        NotificationBody<User> voteResult = gameService.processVotes(gameId);
        assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
        assertEquals(users.get(1), voteResult.getData());
    }

    @Test
    void processVotes_tie() {
        addUsers(6);
        List<User> users = gameService.getAllUsers(gameId);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        gameService.generateCharacters(gameId, characterDistribution);
        gameService.acceptVote(gameId, users.get(0).getID(), users.get(1).getID());
        gameService.acceptVote(gameId, users.get(1).getID(), users.get(2).getID());
        gameService.acceptVote(gameId, users.get(2).getID(), users.get(2).getID());
        gameService.acceptVote(gameId, users.get(3).getID(), users.get(2).getID());
        gameService.acceptVote(gameId, users.get(4).getID(), users.get(1).getID());
        gameService.acceptVote(gameId, users.get(5).getID(), users.get(1).getID());
        NotificationBody<User> voteResult = gameService.processVotes(gameId);
        assertEquals(NotificationType.INVALID_VOTE, voteResult.getNotificationType());
        assertNull(voteResult.getData());
    }

    @Test
    void processVote_includeNobility() {
        addUsers(6);
        List<User> users = gameService.getAllUsers(gameId);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.NOBILITY, 1);
        gameService.generateCharacters(gameId, characterDistribution);
        gameService.acceptVote(gameId, users.get(0).getID(), users.get(1).getID());
        gameService.acceptVote(gameId, users.get(1).getID(), users.get(2).getID());
        gameService.acceptVote(gameId, users.get(2).getID(), users.get(2).getID());
        gameService.acceptVote(gameId, users.get(3).getID(), users.get(2).getID());
        gameService.acceptVote(gameId, users.get(4).getID(), users.get(1).getID());
        gameService.acceptVote(gameId, users.get(5).getID(), users.get(1).getID());
        NotificationBody<User> voteResult = gameService.processVotes(gameId);
        assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
        assertEquals(users.get(1), voteResult.getData());
    }

    @Test
    void addUser() {
        gameService.addUser(gameId, 1L);
        assertEquals(1, gameService.getAllUsers(gameId).size());
    }

    @Test
    void removeUser() {
        gameService.addUser(gameId, 1L);
        gameService.addUser(gameId, 2L);
        assertEquals(2, gameService.getAllUsers(gameId).size());
        NotificationBody<User> removeNotification = gameService.removeUser(gameId, 1L);
        assertEquals(1, gameService.getAllUsers(gameId).size());
        assertEquals(2L, gameService.getAllUsers(gameId).get(0).getID());
        assertEquals(gameId, removeNotification.getGameId());
        assertNull(removeNotification.getReceiver());
        assertEquals(NotificationType.USER_REMOVED, removeNotification.getNotificationType());
        assertEquals(1L, removeNotification.getData().getID());
    }
}

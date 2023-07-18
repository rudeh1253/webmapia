package com.nsl.webmapia.gameoperation.service;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.room.service.RoomService;
import com.nsl.webmapia.room.service.RoomServiceImpl;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.gameoperation.dto.response.CharacterGenerationResponseDTO;
import com.nsl.webmapia.gameoperation.dto.response.UserResponseDTO;
import com.nsl.webmapia.gameoperation.dto.response.VoteResultResponseDTO;
import com.nsl.webmapia.gameoperation.repository.MemoryGameRepository;
import com.nsl.webmapia.user.service.UserService;
import com.nsl.webmapia.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceImplConcurrencyTest {
    GameService gameService;
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
        GameRepository repository = new MemoryGameRepository();
        gameService = new GameServiceImpl(characters, repository);
        roomService = new RoomServiceImpl(characters, repository);
        userService = new UserServiceImpl(repository);
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
            Long gameId = roomService.createNewRoom("asdf", 1L);
            gameIds.add(gameId);
            addUsers(gameId, 16);
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        List<List<CharacterGenerationResponseDTO>> characterNotifications = Collections.synchronizedList(new LinkedList<>());
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

        for (List<CharacterGenerationResponseDTO> notificationList : characterNotifications) {
            assertEquals(16, notificationList.size());
            for (CharacterGenerationResponseDTO notification : notificationList) {
                assertEquals(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED, notification.getNotificationType());
                assertTrue(userIds.contains(notification.getReceiverId()));
                characterDistribution.keySet().forEach(e -> {
                    List<CharacterGenerationResponseDTO> filtered = notificationList.stream()
                            .filter(t -> t.getCharacterCode() == e)
                            .toList();
                    assertEquals(characterDistribution.get(e), filtered.size());
                });
            }
        }
    }

    void addUsers(Long gameId, int num) {
        for (int i = 0; i < num; i++) {
            userService.addUser(gameId, (long)(i + 1));
        }
    }

    @Test
    void stepForward() {
    }

    @Test
    void processVotes() {
        List<Long> gameIds = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            gameIds.add(roomService.createNewRoom("asdf", 1L));
        }
        gameIds.forEach(id -> addUsers(id, 5));
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        gameIds.forEach(id -> gameService.generateCharacters(id, characterDistribution));

        ExecutorService executor1 = Executors.newCachedThreadPool();
        gameIds.forEach(id -> executor1.submit(() -> {
            List<UserResponseDTO> users = userService.getAllUsers(id);
            gameService.acceptVote(id, users.get(0).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(1).getUserId(), users.get(2).getUserId());
            gameService.acceptVote(id, users.get(2).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(3).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(4).getUserId(), users.get(1).getUserId());
        }));
        executor1.shutdown();
        try {
            executor1.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorService executor2 = Executors.newCachedThreadPool();
        gameIds.forEach(id -> executor2.submit(() -> {
            List<UserResponseDTO> users = userService.getAllUsers(id);
            VoteResultResponseDTO voteResult = gameService.processVotes(id);
            assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
            assertEquals(users.get(1).getUserId(), voteResult.getIdOfUserToBeExecuted());
        }));
        executor2.shutdown();
    }

    @Test
    void processVotes_tie() {
        List<Long> gameIds = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            gameIds.add(roomService.createNewRoom("asdf", 1L));
        }
        gameIds.forEach(id -> addUsers(id, 6));
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        gameIds.forEach(id -> gameService.generateCharacters(id, characterDistribution));

        ExecutorService executor1 = Executors.newCachedThreadPool();
        gameIds.forEach(id -> executor1.submit(() -> {
            List<UserResponseDTO> users = userService.getAllUsers(id);
            gameService.acceptVote(id, users.get(0).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(1).getUserId(), users.get(2).getUserId());
            gameService.acceptVote(id, users.get(2).getUserId(), users.get(2).getUserId());
            gameService.acceptVote(id, users.get(3).getUserId(), users.get(2).getUserId());
            gameService.acceptVote(id, users.get(4).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(5).getUserId(), users.get(1).getUserId());
        }));
        executor1.shutdown();
        try {
            executor1.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorService executor2 = Executors.newCachedThreadPool();
        gameIds.forEach(id -> executor2.submit(() -> {
            List<UserResponseDTO> users = userService.getAllUsers(id);
            VoteResultResponseDTO voteResult = gameService.processVotes(id);
            assertEquals(NotificationType.INVALID_VOTE, voteResult.getNotificationType());
            assertNull(voteResult.getIdOfUserToBeExecuted());
        }));
        executor2.shutdown();
    }

    @Test
    void processVote_includeNobility() {
        List<Long> gameIds = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            gameIds.add(roomService.createNewRoom("asdf", 1L));
        }
        gameIds.forEach(id -> addUsers(id, 6));
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.NOBILITY, 1);
        gameIds.forEach(id -> gameService.generateCharacters(id, characterDistribution));

        ExecutorService executor1 = Executors.newCachedThreadPool();
        Map<Long, UserResponseDTO> executed = new ConcurrentHashMap<>();
        gameIds.forEach(id -> executor1.submit(() -> {
            List<UserResponseDTO> users = userService.getAllUsers(id);
            gameService.acceptVote(id, users.get(0).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(1).getUserId(), users.get(2).getUserId());
            gameService.acceptVote(id, users.get(2).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(3).getUserId(), users.get(2).getUserId());
            gameService.acceptVote(id, users.get(4).getUserId(), users.get(1).getUserId());
            gameService.acceptVote(id, users.get(5).getUserId(), users.get(2).getUserId());
            for (UserResponseDTO u : users) {
                if (u.getCharacterCode() == CharacterCode.NOBILITY) {
                    executed.put(id, u);
                    break;
                }
            }
        }));
        executor1.shutdown();
        try {
            executor1.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorService executor2 = Executors.newCachedThreadPool();
        gameIds.forEach(id -> executor2.submit(() -> {
            List<UserResponseDTO> users = userService.getAllUsers(id);
            VoteResultResponseDTO voteResult = gameService.processVotes(id);
            assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
            assertEquals(users.get(1).getUserId(), voteResult.getIdOfUserToBeExecuted());
        }));
        executor2.shutdown();
    }
}

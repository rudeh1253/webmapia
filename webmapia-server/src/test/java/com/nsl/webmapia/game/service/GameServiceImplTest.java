package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.GameNotification;
import com.nsl.webmapia.game.domain.notification.GameNotificationType;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.dto.CharacterGenerationResponseDTO;
import com.nsl.webmapia.game.dto.UserResponseDTO;
import com.nsl.webmapia.game.dto.VoteResultResponseDTO;
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

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {
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
        List<CharacterGenerationResponseDTO> charNotifications = gameService.generateCharacters(gameId, characterDistribution);
        List<Long> userIds = new ArrayList<>(16);
        for (UserResponseDTO u : gameService.getAllUsers(gameId)) {
            userIds.add(u.getUserId());
        }

        charNotifications.forEach(e -> {
            assertEquals(GameNotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED, e.getNotificationType());
            assertEquals(gameId, e.getGameId());
            assertTrue(userIds.contains(e.getReceiverId()));
        });

        characterDistribution.keySet().forEach(k -> {
            assertEquals(characterDistribution.get(k),
                    charNotifications.stream().filter(e -> e.getCharacterCode() == k)
                    .toList().size());
        });
    }

    @Test
    void stepForward() {
    }

    @Test
    void processVotes() {
        addUsers(5);
        List<UserResponseDTO> users = gameService.getAllUsers(gameId);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        gameService.generateCharacters(gameId, characterDistribution);
        gameService.acceptVote(gameId, users.get(0).getUserId(), users.get(1).getUserId());
        gameService.acceptVote(gameId, users.get(1).getUserId(), users.get(2).getUserId());
        gameService.acceptVote(gameId, users.get(2).getUserId(), users.get(1).getUserId());
        gameService.acceptVote(gameId, users.get(3).getUserId(), users.get(1).getUserId());
        gameService.acceptVote(gameId, users.get(4).getUserId(), users.get(1).getUserId());
        VoteResultResponseDTO voteResult = gameService.processVotes(gameId);
        System.out.println(voteResult);
        assertEquals(GameNotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
        assertEquals(users.get(1).getUserId(), voteResult.getIdOfUserToBeExecuted());
    }

    @Test
    void processVotes_tie() {
        addUsers(6);
        List<UserResponseDTO> users = gameService.getAllUsers(gameId);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        gameService.generateCharacters(gameId, characterDistribution);
        gameService.acceptVote(gameId, users.get(0).getUserId(), users.get(1).getUserId());
        gameService.acceptVote(gameId, users.get(1).getUserId(), users.get(2).getUserId());
        gameService.acceptVote(gameId, users.get(2).getUserId(), users.get(2).getUserId());
        gameService.acceptVote(gameId, users.get(3).getUserId(), users.get(2).getUserId());
        gameService.acceptVote(gameId, users.get(4).getUserId(), users.get(1).getUserId());
        gameService.acceptVote(gameId, users.get(5).getUserId(), users.get(1).getUserId());
        VoteResultResponseDTO voteResult = gameService.processVotes(gameId);
        assertEquals(GameNotificationType.INVALID_VOTE, voteResult.getNotificationType());
        assertNull(voteResult.getIdOfUserToBeExecuted());
    }

    @Test
    void processVote_includeNobility() {
        addUsers(6);
        List<UserResponseDTO> users = gameService.getAllUsers(gameId);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.NOBILITY, 1);
        gameService.generateCharacters(gameId, characterDistribution);
        gameService.acceptVote(gameId, users.get(0).getUserId(), users.get(1).getUserId());
        gameService.acceptVote(gameId, users.get(1).getUserId(), users.get(2).getUserId());
        gameService.acceptVote(gameId, users.get(2).getUserId(), users.get(2).getUserId());
        gameService.acceptVote(gameId, users.get(3).getUserId(), users.get(2).getUserId());
        gameService.acceptVote(gameId, users.get(4).getUserId(), users.get(1).getUserId());
        gameService.acceptVote(gameId, users.get(5).getUserId(), users.get(1).getUserId());
        VoteResultResponseDTO voteResult = gameService.processVotes(gameId);
        assertEquals(GameNotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
        assertEquals(users.get(1).getUserId(), voteResult.getIdOfUserToBeExecuted());
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
        UserResponseDTO removeNotification = gameService.removeUser(gameId, 1L);
        assertEquals(1, gameService.getAllUsers(gameId).size());
        assertEquals(2L, gameService.getAllUsers(gameId).get(0).getUserId());
        assertEquals(gameId, removeNotification.getGameId());
        assertEquals(GameNotificationType.USER_REMOVED, removeNotification.getNotificationType());
        assertEquals(1L, removeNotification.getUserId());
    }

    @Test
    void createGames() {
        List<Long> gameIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            gameIds.add(gameService.createNewGame());
        }
        gameIds.add(gameId);
        List<GameManager> allGames = gameService.getAllGames();
        allGames.forEach(g -> assertTrue(gameIds.contains(g.getGameId())));
    }
}

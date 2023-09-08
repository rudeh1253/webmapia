package com.nsl.webmapia.gameoperation.service;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.room.service.RoomService;
import com.nsl.webmapia.room.service.RoomServiceImpl;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.gameoperation.dto.CharacterGenerationResponseDTO;
import com.nsl.webmapia.room.dto.RoomInfoResponseDTO;
import com.nsl.webmapia.user.dto.UserResponseDTO;
import com.nsl.webmapia.gameoperation.dto.VoteResultResponseDTO;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.gameoperation.repository.MemoryGameRepository;
import com.nsl.webmapia.user.service.UserService;
import com.nsl.webmapia.user.service.UserServiceImpl;
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
        gameService = new GameServiceImpl(gameRepository, null);
        userService = new UserServiceImpl(gameRepository);
        gameId = roomService.createNewRoom("asdf", 1L);
        userService.addUser(gameId, 1L);
    }

    void addUsers(int num) {
        for (int i = 0; i < num; i++) {
            userService.addUser(gameId, (long)(i + 1));
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
        for (UserResponseDTO u : userService.getAllUsers(gameId)) {
            userIds.add(u.getUserId());
        }

        charNotifications.forEach(e -> {
            assertEquals(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED, e.getNotificationType());
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
        List<UserResponseDTO> users = userService.getAllUsers(gameId);
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
        assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
        assertEquals(users.get(1).getUserId(), voteResult.getIdOfUserToBeExecuted());
    }

    @Test
    void processVotes_tie() {
        addUsers(6);
        List<UserResponseDTO> users = userService.getAllUsers(gameId);
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
        assertEquals(NotificationType.INVALID_VOTE, voteResult.getNotificationType());
        assertNull(voteResult.getIdOfUserToBeExecuted());
    }

    @Test
    void processVote_includeNobility() {
        addUsers(6);
        List<UserResponseDTO> users = userService.getAllUsers(gameId);
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
        assertEquals(NotificationType.EXECUTE_BY_VOTE, voteResult.getNotificationType());
        assertEquals(users.get(1).getUserId(), voteResult.getIdOfUserToBeExecuted());
    }

    @Test
    void createGames() {
        List<Long> gameIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            gameIds.add(roomService.createNewRoom("asdf", 1L));
        }
        gameIds.forEach(id -> userService.addUser(id, 1L));
        gameIds.add(gameId);
        List<RoomInfoResponseDTO> allGames = roomService.getAllRoomInfo();
        allGames.forEach(g -> assertTrue(gameIds.contains(g.getRoomId())));
    }
}

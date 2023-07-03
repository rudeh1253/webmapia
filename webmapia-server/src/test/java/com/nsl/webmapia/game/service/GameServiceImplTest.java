package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.SkillManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.PrivateNotificationBody;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {
    private SkillManager skillManager;
    private GameService gameService;
    private UserRepository userRepository;

    @BeforeEach
    public void intialize() {
        skillManager = new SkillManager();
        userRepository = new MemoryUserRepository();
        gameService = new GameServiceImpl(
                new Wolf(skillManager),
                new Betrayer(skillManager),
                new Citizen(skillManager),
                new Detective(skillManager),
                new Follower(skillManager),
                new Guard(skillManager),
                new HumanMouse(skillManager),
                new Mediumship(skillManager),
                new Murderer(skillManager),
                new Nobility(skillManager),
                new Predictor(skillManager),
                new SecretSociety(skillManager),
                new Soldier(skillManager),
                new Successor(skillManager),
                new Templar(skillManager),
                skillManager,
                userRepository
        );
    }

    @Test
    void allocateCharacterToEachUser() {
        long[] userIds = new long[7];
        for (int i = 0; i < userIds.length; i++) {
            userIds[i] = gameService.addUser();
        }
        // Wolf: 1
        // Betrayer: 1
        // Guard: 1
        // SecretSociety: 2
        // HumanMouse: 1
        // Citizen: 1
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        characterDistribution.put(CharacterCode.SECRET_SOCIETY, 2);
        characterDistribution.put(CharacterCode.HUMAN_MOUSE, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        List<PrivateNotificationBody<Character>> privateNotificationBodies =
                gameService.allocateCharacterToEachUser(characterDistribution);
        assertEquals(7, privateNotificationBodies.size());
        assertEquals(1,privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.WOLF)
                .toList().size());
        assertEquals(1,privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.BETRAYER)
                .toList().size());
        assertEquals(1,privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.GUARD)
                .toList().size());
        assertEquals(2,privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.SECRET_SOCIETY)
                .toList().size());
        assertEquals(1,privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.HUMAN_MOUSE)
                .toList().size());
        assertEquals(1,privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.CITIZEN)
                .toList().size());
        for (PrivateNotificationBody<Character> body : privateNotificationBodies) {
            User user = userRepository.findById(body.getReceiver().getID()).get();
            assertEquals(user.getCharacter().getCharacterCode(), body.getData().getCharacterCode());
        }
    }
}
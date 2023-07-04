package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.SkillManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.NotificationBody;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
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
        addUsers(7);
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
        List<NotificationBody<Character>> privateNotificationBodies =
                gameService.generateCharacters(characterDistribution);
        assertEquals(7, privateNotificationBodies.size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.WOLF)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.BETRAYER)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.GUARD)
                .toList().size());
        assertEquals(2, privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.SECRET_SOCIETY)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.HUMAN_MOUSE)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getData().getCharacterCode() == CharacterCode.CITIZEN)
                .toList().size());
        for (NotificationBody<Character> body : privateNotificationBodies) {
            User user = userRepository.findById(body.getReceiver().getID()).get();
            assertEquals(user.getCharacter().getCharacterCode(), body.getData().getCharacterCode());
        }
    }

    @Test
    public void randomnessOfAllocateCharacterToEachUser() {
        addUsers(7);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        characterDistribution.put(CharacterCode.SECRET_SOCIETY, 2);
        characterDistribution.put(CharacterCode.HUMAN_MOUSE, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        List<NotificationBody<Character>> privateNotificationBodies1 =
                gameService.generateCharacters(characterDistribution);
        List<NotificationBody<Character>> privateNotificationBodies2 =
                gameService.generateCharacters(characterDistribution);
        assertEquals(7, privateNotificationBodies1.size());
        assertEquals(7, privateNotificationBodies2.size());

        boolean existsDifference = false;
        for (int i = 0; i < privateNotificationBodies1.size(); i++) {
            NotificationBody<Character> p1 = privateNotificationBodies1.get(i);
            for (int j = 0; j < privateNotificationBodies2.size(); j++) {
                NotificationBody<Character> p2 = privateNotificationBodies2.get(j);
                if (p1.getReceiver().getID().equals(p2.getReceiver().getID())
                && p1.getData().getCharacterCode() != p2.getData().getCharacterCode()) {
                    existsDifference = true;
                }
            }
        }
        assertTrue(existsDifference);
    }

    private void addUsers(int num) {
        for (int i = 0; i < num; i++) {
            gameService.addUser();
        }
    }

    @Test
    public void testActivateSkill() {
        setting();

        List<ActivatedSkillInfo> activatedSkillInfoList = ((GameServiceImpl)gameService).getActivatedSkills();
        assertEquals(16, activatedSkillInfoList.size());
    }

    @Test
    public void testSort() {
        setting();

        List<ActivatedSkillInfo> activatedSkillInfoList = ((GameServiceImpl)gameService).getActivatedSkills();
        activatedSkillInfoList.sort((left, right) -> {
            if (left.getSkillType() == SkillType.EXTERMINATE) {
                return -1;
            } else if (right.getSkillType() == SkillType.EXTERMINATE) {
                return 1;
            } else if (left.getSkillType() == SkillType.KILL) {
                return -1;
            } else if (right.getSkillType() == SkillType.KILL) {
                return 1;
            } else if (left.getSkillType() == SkillType.GUARD) {
                return -1;
            } else if (right.getSkillType() == SkillType.GUARD) {
                return -1;
            } else {
                return 0;
            }
        });
        assertEquals(SkillType.EXTERMINATE, activatedSkillInfoList.get(0).getSkillType());
        assertEquals(SkillType.KILL, activatedSkillInfoList.get(1).getSkillType());
        assertEquals(SkillType.GUARD, activatedSkillInfoList.get(2).getSkillType());
        assertEquals(SkillType.GUARD, activatedSkillInfoList.get(3).getSkillType());
        for (int i = 4; i < activatedSkillInfoList.size(); i++) {
            SkillType s = activatedSkillInfoList.get(i).getSkillType();
            assertTrue(s != SkillType.EXTERMINATE && s != SkillType.KILL && s != SkillType.GUARD);
        }
        assertEquals(16, activatedSkillInfoList.size());
    }

    @Test
    public void testProcessSkills() {
        setting();
        List<NotificationBody<SkillEffect>> notificationBodies = gameService.processSkills();
        List<SkillEffect> skillEffects = new ArrayList<>();
        notificationBodies.forEach(e -> skillEffects.add(e.getData()));
        User predictor = userRepository.findByCharacterCode(CharacterCode.PREDICTOR).get(0);
        User murderer = userRepository.findByCharacterCode(CharacterCode.MURDERER).get(0);
        User wolf = userRepository.findByCharacterCode(CharacterCode.WOLF).get(0);
        User detective = userRepository.findByCharacterCode(CharacterCode.DETECTIVE).get(0);
        User soldier = userRepository.findByCharacterCode(CharacterCode.SOLDIER).get(0);
        User guard = userRepository.findByCharacterCode(CharacterCode.GUARD).get(0);
        User citizen = userRepository.findByCharacterCode(CharacterCode.CITIZEN).get(0);
        User betrayer = userRepository.findByCharacterCode(CharacterCode.BETRAYER).get(0);
        User successor = userRepository.findByCharacterCode(CharacterCode.SUCCESSOR).get(0);
        User templar = userRepository.findByCharacterCode(CharacterCode.TEMPLAR).get(0);
        User secretSociety1 = userRepository.findByCharacterCode(CharacterCode.SECRET_SOCIETY).get(0);
        User secretSociety2 = userRepository.findByCharacterCode(CharacterCode.SECRET_SOCIETY).get(1);
        User humanMouse = userRepository.findByCharacterCode(CharacterCode.HUMAN_MOUSE).get(0);
        User nobility = userRepository.findByCharacterCode(CharacterCode.NOBILITY).get(0);
        User mediumship = userRepository.findByCharacterCode(CharacterCode.MEDIUMSHIP).get(0);
        User follower = userRepository.findByCharacterCode(CharacterCode.FOLLOWER).get(0);
        skillEffects.forEach(e -> {
            switch (e.getCharacterEffectAfterNightType()) {
                case EXTERMINATE:
                    assertNull(e.getReceiverUser());
                    assertEquals(predictor, e.getSkillTargetUser());
                    assertEquals(CharacterCode.PREDICTOR, e.getSkillTargetCharacterCode());
                    assertEquals(murderer, e.getSkillActivatorUser());
                    break;
                case KILL:
                    if (e.getSkillActivatorUser() == predictor) {
                        assertNull(e.getReceiverUser());
                        assertEquals(predictor, e.getSkillActivatorUser());
                        assertEquals(humanMouse, e.getSkillTargetUser());
                        assertEquals(CharacterCode.HUMAN_MOUSE, e.getSkillTargetCharacterCode());
                    } else {
                        assertNull(e.getReceiverUser());
                        assertEquals(wolf, e.getSkillActivatorUser());
                        assertEquals(detective, e.getSkillTargetUser());
                        assertEquals(CharacterCode.DETECTIVE, e.getSkillTargetCharacterCode());
                    }
                    break;
                case GUARD:
                    assertEquals(soldier, e.getReceiverUser());
                    assertEquals(soldier, e.getSkillTargetUser());
                    assertEquals(soldier, e.getSkillActivatorUser());
                    break;
                case FAIL_TO_GUARD:
                    assertEquals(guard, e.getReceiverUser());
                    assertEquals(wolf, e.getSkillTargetUser());
                    assertEquals(guard, e.getSkillActivatorUser());
                    break;
                case FAIL_TO_INVESTIGATE:
                    if (e.getSkillActivatorUser() == mediumship) {
                        assertEquals(mediumship, e.getSkillActivatorUser());
                        assertEquals(mediumship, e.getReceiverUser());
                        assertEquals(templar, e.getSkillTargetUser());
                    } else if (follower == e.getSkillActivatorUser()) {
                        assertEquals(follower, e.getSkillActivatorUser());
                        assertEquals(predictor, e.getSkillTargetUser());
                        assertEquals(follower, e.getReceiverUser());
                    } else {
                        assertEquals(detective, e.getSkillActivatorUser());
                        assertEquals(wolf, e.getSkillTargetUser());
                        assertEquals(detective, e.getReceiverUser());
                    }
                    break;
                case ENTER_WOLF_CHAT:
                    assertEquals(betrayer, e.getReceiverUser());
                    assertEquals(betrayer, e.getSkillActivatorUser());
                    assertEquals(wolf, e.getSkillTargetUser());
                    break;
                case NOTIFY:
                case NONE:
            }
        });
    }

    private void setting() {
        addUsers(16);
        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.FOLLOWER, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        characterDistribution.put(CharacterCode.SOLDIER, 1);
        characterDistribution.put(CharacterCode.SUCCESSOR, 1);
        characterDistribution.put(CharacterCode.TEMPLAR, 1);
        characterDistribution.put(CharacterCode.SECRET_SOCIETY, 2);
        characterDistribution.put(CharacterCode.HUMAN_MOUSE, 1);
        characterDistribution.put(CharacterCode.NOBILITY, 1);
        characterDistribution.put(CharacterCode.MEDIUMSHIP, 1);
        characterDistribution.put(CharacterCode.MURDERER, 1);
        characterDistribution.put(CharacterCode.PREDICTOR, 1);
        characterDistribution.put(CharacterCode.DETECTIVE, 1);
        gameService.generateCharacters(characterDistribution);
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            assertNotNull(user.getCharacter());
        }

        User wolfUser = userRepository.findByCharacterCode(CharacterCode.WOLF).get(0);
        User betrayerUser = userRepository.findByCharacterCode(CharacterCode.BETRAYER).get(0);
        User followerUser = userRepository.findByCharacterCode(CharacterCode.FOLLOWER).get(0);
        User citizenUser = userRepository.findByCharacterCode(CharacterCode.CITIZEN).get(0);
        User guardUser = userRepository.findByCharacterCode(CharacterCode.GUARD).get(0);
        User soldierUser = userRepository.findByCharacterCode(CharacterCode.SOLDIER).get(0);
        User successorUser = userRepository.findByCharacterCode(CharacterCode.SUCCESSOR).get(0);
        User templarUser = userRepository.findByCharacterCode(CharacterCode.TEMPLAR).get(0);
        User secretSocietyUser1 = userRepository.findByCharacterCode(CharacterCode.SECRET_SOCIETY).get(0);
        User secretSocietyUser2 = userRepository.findByCharacterCode(CharacterCode.SECRET_SOCIETY).get(1);
        User humanMouseUser = userRepository.findByCharacterCode(CharacterCode.HUMAN_MOUSE).get(0);
        User nobilityUser = userRepository.findByCharacterCode(CharacterCode.NOBILITY).get(0);
        User mediumshipUser = userRepository.findByCharacterCode(CharacterCode.MEDIUMSHIP).get(0);
        User murdererUser = userRepository.findByCharacterCode(CharacterCode.MURDERER).get(0);
        User predictorUser = userRepository.findByCharacterCode(CharacterCode.PREDICTOR).get(0);
        User detectiveUser = userRepository.findByCharacterCode(CharacterCode.DETECTIVE).get(0);

        // wolf -> detective: kill
        // betrayer -> wolf: enter_wolf_chat
        // follower -> predictor: enter_wolf_chat
        // citizen -> predictor: none
        // guard -> wolf: guard
        // soldier -> soldier: guard
        // successor -> predictor: none
        // templar -> predictor: none
        // secret_society1 -> predictor: none
        // secret_society2 -> predictor: none
        // human_mouse -> predictor: none
        // nobility -> predictor: none
        // mediumship -> templar: investigate_dead-character
        // murderer -> predictor: exterminate
        // predictor -> human_mouse: investigate_alive_character
        // detective -> wolf: investigate_alive_character
        gameService.activateSkill(wolfUser.getID(), detectiveUser.getID(), SkillType.KILL);
        gameService.activateSkill(betrayerUser.getID(), wolfUser.getID(), SkillType.ENTER_WOLF_CHAT);
        gameService.activateSkill(followerUser.getID(), predictorUser.getID(), SkillType.ENTER_WOLF_CHAT);
        gameService.activateSkill(citizenUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameService.activateSkill(guardUser.getID(), wolfUser.getID(), SkillType.GUARD);
        gameService.activateSkill(soldierUser.getID(), soldierUser.getID(), SkillType.GUARD);
        gameService.activateSkill(successorUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameService.activateSkill(templarUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameService.activateSkill(secretSocietyUser1.getID(), predictorUser.getID(), SkillType.NONE);
        gameService.activateSkill(secretSocietyUser2.getID(), predictorUser.getID(), SkillType.NONE);
        gameService.activateSkill(humanMouseUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameService.activateSkill(nobilityUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameService.activateSkill(mediumshipUser.getID(), templarUser.getID(), SkillType.INVESTIGATE_DEAD_CHARACTER);
        gameService.activateSkill(murdererUser.getID(), predictorUser.getID(), SkillType.EXTERMINATE);
        gameService.activateSkill(predictorUser.getID(), humanMouseUser.getID(), SkillType.INVESTIGATE_ALIVE_CHARACTER);
        gameService.activateSkill(detectiveUser.getID(), wolfUser.getID(), SkillType.INVESTIGATE_ALIVE_CHARACTER);
    }
}

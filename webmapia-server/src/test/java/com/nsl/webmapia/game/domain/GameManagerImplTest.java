package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerImplTest {
    private SkillManager skillManager;
    private GameManager gameManager;
    private UserRepository userRepository;

    @BeforeEach
    public void intialize() {
        skillManager = new SkillManager();
        userRepository = new MemoryUserRepository();
        Map<CharacterCode, Character> characters = Map.ofEntries(
                Map.entry(CharacterCode.WOLF, new Wolf(skillManager)),
                Map.entry(CharacterCode.BETRAYER, new Betrayer(skillManager)),
                Map.entry(CharacterCode.DETECTIVE, new Detective(skillManager)),
                Map.entry(CharacterCode.FOLLOWER, new Follower(skillManager)),
                Map.entry(CharacterCode.CITIZEN, new Citizen(skillManager)),
                Map.entry(CharacterCode.GUARD, new Guard(skillManager)),
                Map.entry(CharacterCode.HUMAN_MOUSE, new HumanMouse(skillManager)),
                Map.entry(CharacterCode.MEDIUMSHIP, new Mediumship(skillManager)),
                Map.entry(CharacterCode.MURDERER, new Murderer(skillManager)),
                Map.entry(CharacterCode.NOBILITY, new Nobility(skillManager)),
                Map.entry(CharacterCode.PREDICTOR, new Predictor(skillManager)),
                Map.entry(CharacterCode.SECRET_SOCIETY, new SecretSociety(skillManager)),
                Map.entry(CharacterCode.SOLDIER, new Soldier(skillManager)),
                Map.entry(CharacterCode.SUCCESSOR, new Successor(skillManager)),
                Map.entry(CharacterCode.TEMPLAR, new Templar(skillManager)));
        gameManager = new GameManagerImpl(
                1L,
                characters,
                skillManager,
                userRepository
        );
    }

    @Test
    void allocateCharacterToEachUser() {
        addUsers(7, gameManager);
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
        List<User> privateNotificationBodies =
                gameManager.generateCharacters(characterDistribution);
        assertEquals(7, privateNotificationBodies.size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getCharacter().getCharacterCode() == CharacterCode.WOLF)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getCharacter().getCharacterCode() == CharacterCode.BETRAYER)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getCharacter().getCharacterCode() == CharacterCode.GUARD)
                .toList().size());
        assertEquals(2, privateNotificationBodies.stream()
                .filter(n -> n.getCharacter().getCharacterCode() == CharacterCode.SECRET_SOCIETY)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getCharacter().getCharacterCode() == CharacterCode.HUMAN_MOUSE)
                .toList().size());
        assertEquals(1, privateNotificationBodies.stream()
                .filter(n -> n.getCharacter().getCharacterCode() == CharacterCode.CITIZEN)
                .toList().size());
        for (User body : privateNotificationBodies) {
            User user = userRepository.findById(body.getID()).get();
            assertEquals(user.getCharacter().getCharacterCode(), body.getCharacter().getCharacterCode());
        }
    }

    @Test
    public void randomnessOfAllocateCharacterToEachUser() {
        Map<CharacterCode, Character> characters = Map.ofEntries(
                Map.entry(CharacterCode.WOLF, new Wolf(skillManager)),
                Map.entry(CharacterCode.BETRAYER, new Betrayer(skillManager)),
                Map.entry(CharacterCode.DETECTIVE, new Detective(skillManager)),
                Map.entry(CharacterCode.FOLLOWER, new Follower(skillManager)),
                Map.entry(CharacterCode.CITIZEN, new Citizen(skillManager)),
                Map.entry(CharacterCode.GUARD, new Guard(skillManager)),
                Map.entry(CharacterCode.HUMAN_MOUSE, new HumanMouse(skillManager)),
                Map.entry(CharacterCode.MEDIUMSHIP, new Mediumship(skillManager)),
                Map.entry(CharacterCode.MURDERER, new Murderer(skillManager)),
                Map.entry(CharacterCode.NOBILITY, new Nobility(skillManager)),
                Map.entry(CharacterCode.PREDICTOR, new Predictor(skillManager)),
                Map.entry(CharacterCode.SECRET_SOCIETY, new SecretSociety(skillManager)),
                Map.entry(CharacterCode.SOLDIER, new Soldier(skillManager)),
                Map.entry(CharacterCode.SUCCESSOR, new Successor(skillManager)),
                Map.entry(CharacterCode.TEMPLAR, new Templar(skillManager)));

        Map<CharacterCode, Integer> characterDistribution = new HashMap<>();
        characterDistribution.put(CharacterCode.WOLF, 1);
        characterDistribution.put(CharacterCode.BETRAYER, 1);
        characterDistribution.put(CharacterCode.GUARD, 1);
        characterDistribution.put(CharacterCode.SECRET_SOCIETY, 2);
        characterDistribution.put(CharacterCode.HUMAN_MOUSE, 1);
        characterDistribution.put(CharacterCode.CITIZEN, 1);

        GameManager game1 = new GameManagerImpl(1L, characters, null, new MemoryUserRepository());
        GameManager game2 = new GameManagerImpl(2L, characters, null, new MemoryUserRepository());
        addUsers(7, game1);
        addUsers(7, game2);
        List<User> privateNotificationBodies1 =
                game1.generateCharacters(characterDistribution);
        List<User> privateNotificationBodies2 =
                game2.generateCharacters(characterDistribution);
        assertEquals(7, privateNotificationBodies1.size());
        assertEquals(7, privateNotificationBodies2.size());

        boolean existsDifference = false;
        for (int i = 0; i < privateNotificationBodies1.size(); i++) {
            User p1 = privateNotificationBodies1.get(i);
            for (int j = 0; j < privateNotificationBodies2.size(); j++) {
                User p2 = privateNotificationBodies2.get(j);
                if (p1.getID().equals(p2.getID())
                && p1.getCharacter().getCharacterCode() != p2.getCharacter().getCharacterCode()) {
                    existsDifference = true;
                }
            }
        }
        assertTrue(existsDifference);
    }

    private void addUsers(int num, GameManager game) {
        for (int i = 0; i < num; i++) {
            game.addUser(Long.valueOf(i));
        }
    }

    @Test
    public void testActivateSkill() {
        setting();

        List<ActivatedSkillInfo> activatedSkillInfoList = ((GameManagerImpl) gameManager).getActivatedSkills();
        assertEquals(16, activatedSkillInfoList.size());
    }

    @Test
    public void testSort() {
        setting();

        List<ActivatedSkillInfo> activatedSkillInfoList = ((GameManagerImpl) gameManager).getActivatedSkills();
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
        List<SkillEffect> skillEffects = gameManager.processSkills();
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

    @Test
    void addUsersConcurrent() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 16; i++) {
            final int userId = i + 1;
            executor.submit(() -> gameManager.addUser((long)userId));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(16, gameManager.getAllUsers().size());
    }

    private void setting() {
        addUsers(16, gameManager);
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
        gameManager.generateCharacters(characterDistribution);
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
        gameManager.activateSkill(wolfUser.getID(), detectiveUser.getID(), SkillType.KILL);
        gameManager.activateSkill(betrayerUser.getID(), wolfUser.getID(), SkillType.ENTER_WOLF_CHAT);
        gameManager.activateSkill(followerUser.getID(), predictorUser.getID(), SkillType.ENTER_WOLF_CHAT);
        gameManager.activateSkill(citizenUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameManager.activateSkill(guardUser.getID(), wolfUser.getID(), SkillType.GUARD);
        gameManager.activateSkill(soldierUser.getID(), soldierUser.getID(), SkillType.GUARD);
        gameManager.activateSkill(successorUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameManager.activateSkill(templarUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameManager.activateSkill(secretSocietyUser1.getID(), predictorUser.getID(), SkillType.NONE);
        gameManager.activateSkill(secretSocietyUser2.getID(), predictorUser.getID(), SkillType.NONE);
        gameManager.activateSkill(humanMouseUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameManager.activateSkill(nobilityUser.getID(), predictorUser.getID(), SkillType.NONE);
        gameManager.activateSkill(mediumshipUser.getID(), templarUser.getID(), SkillType.INVESTIGATE_DEAD_CHARACTER);
        gameManager.activateSkill(murdererUser.getID(), predictorUser.getID(), SkillType.EXTERMINATE);
        gameManager.activateSkill(predictorUser.getID(), humanMouseUser.getID(), SkillType.INVESTIGATE_ALIVE_CHARACTER);
        gameManager.activateSkill(detectiveUser.getID(), wolfUser.getID(), SkillType.INVESTIGATE_ALIVE_CHARACTER);
    }

    @Test
    public void testVote() {
//        addUsers(10);
//        List<User> allUser = userRepository.findAll();
//        for (int i = 0; i < allUser.size(); i++) {
//            gameManager.acceptVote(allUser.get(i).getID(), allUser.get((i + 1) % allUser.size()).getID());
//        }
    }
}

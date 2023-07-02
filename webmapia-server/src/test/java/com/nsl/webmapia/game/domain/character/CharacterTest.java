package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {
    Character wolf;
    Character betrayer;
    Character follower;
    Character predictor;
    Character guard;
    Character mediumship;
    Character detective;
    Character successor;
    Character secretSociety;
    Character nobility;
    Character soldier;
    Character templar;
    Character citizen;
    Character murderer;
    Character humanMouse;
    User wolfUser;
    User betrayerUser;
    User followerUser;
    User predictorUser;
    User guardUser;
    User mediumshipUser;
    User detectiveUser;
    User secretSocientyUser;
    User successorUser;
    User nobilityUser;
    User soldierUser;
    User templarUser;
    User citizenUser;
    User murdererUser;
    User humanMouseUser;

    GameManager gameManager;

    UserRepository userRepository;

    @BeforeEach
    public void initialize() {
        gameManager = new GameManager();
        wolf = new Wolf(gameManager);
        betrayer = new Betrayer(gameManager);
        follower = new Follower(gameManager);
        predictor = new Predictor(gameManager);
        guard = new Guard(gameManager);
        mediumship = new Mediumship(gameManager);
        detective = new Detective(gameManager);
        successor = new Successor(gameManager);
        nobility = new Nobility(gameManager);
        soldier = new Soldier(gameManager);
        secretSociety = new SecretSociety(gameManager);
        templar = new Templar(gameManager);
        citizen = new Citizen(gameManager);
        murderer = new Murderer(gameManager);
        humanMouse = new HumanMouse(gameManager);
        userRepository = new MemoryUserRepository();
        wolfUser = new User(1L);
        wolfUser.setCharacter(wolf);
        betrayerUser = new User(2L);
        betrayerUser.setCharacter(betrayer);
        followerUser = new User(3L);
        followerUser.setCharacter(follower);
        predictorUser = new User(4L);
        predictorUser.setCharacter(predictor);
        guardUser = new User(5L);
        guardUser.setCharacter(guard);
        mediumshipUser = new User(6L);
        mediumshipUser.setCharacter(mediumship);
        detectiveUser = new User(7L);
        detectiveUser.setCharacter(detective);
        secretSocientyUser = new User(8L);
        secretSocientyUser.setCharacter(secretSociety);
        successorUser = new User(9L);
        successorUser.setCharacter(successor);
        nobilityUser = new User(10L);
        nobilityUser.setCharacter(nobility);
        soldierUser = new User(11L);
        soldierUser.setCharacter(soldier);
        templarUser = new User(12L);
        templarUser.setCharacter(templar);
        citizenUser = new User(13L);
        citizenUser.setCharacter(citizen);
        murdererUser = new User(14L);
        murdererUser.setCharacter(murderer);
        humanMouseUser = new User(15L);
        humanMouseUser.setCharacter(humanMouse);
        userRepository.save(wolfUser);
        userRepository.save(betrayerUser);
        userRepository.save(followerUser);
        userRepository.save(predictorUser);
        userRepository.save(guardUser);
        userRepository.save(mediumshipUser);
        userRepository.save(detectiveUser);
        userRepository.save(secretSocientyUser);
        userRepository.save(successorUser);
        userRepository.save(nobilityUser);
        userRepository.save(soldierUser);
        userRepository.save(templarUser);
        userRepository.save(citizenUser);
        userRepository.save(murdererUser);
        userRepository.save(humanMouseUser);
    }

    @Test
    public void betrayerAndFollowerFoundWolf() {
        ActivatedSkillInfo betrayerFoundWolf = betrayerUser.activateSkill(wolfUser, SkillType.ENTER_WOLF_CHAT);
        ActivatedSkillInfo followerFoundWolf = followerUser.activateSkill(wolfUser, SkillType.ENTER_WOLF_CHAT);

        assertTrue(betrayerFoundWolf.getSkillCondition().isSuccess(betrayerUser, wolfUser, SkillType.ENTER_WOLF_CHAT));
        assertTrue(followerFoundWolf.getSkillCondition().isSuccess(followerUser, wolfUser, SkillType.ENTER_WOLF_CHAT));
        assertEquals(betrayerFoundWolf.getActivator(), betrayerUser);
        assertEquals(followerFoundWolf.getActivator(), followerUser);
        assertEquals(betrayerFoundWolf.getTarget(), wolfUser);
        assertEquals(followerFoundWolf.getActivator(), followerUser);
        betrayerFoundWolf.getOnSkillSucceed().onSkillSucceed(betrayerUser, wolfUser, SkillType.ENTER_WOLF_CHAT);
        followerFoundWolf.getOnSkillSucceed().onSkillSucceed(followerUser, wolfUser, SkillType.ENTER_WOLF_CHAT);
        assertEquals(4, gameManager.getSkillNotifications().size());
        List<SkillNotificationBody> forWolf = gameManager.getSkillNotifications().stream()
                .filter(e -> Objects.equals(e.getReceiverUserId(), wolfUser.getID()))
                .toList();
        List<SkillNotificationBody> forBetrayer = gameManager.getSkillNotifications().stream()
                .filter(e -> Objects.equals(e.getReceiverUserId(), betrayerUser.getID()))
                .toList();
        List<SkillNotificationBody> forFollower = gameManager.getSkillNotifications().stream()
                .filter(e -> e.getReceiverUserId() == followerUser.getID())
                .toList();
        assertEquals(2, forWolf.size());
        assertEquals(1, forBetrayer.size());
        assertEquals(1, forFollower.size());
        forWolf.forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), wolfUser.getID());
                    assertTrue(note.getMessage().matches(".+ entered the wolf chat"));
                    System.out.println(note.getSkillTargetUserId());
                    assertEquals(note.getSkillTargetUserId(), wolfUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.NOTIFY);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
        forBetrayer.forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), betrayerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), wolfUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.ENTER_WOLF_CHAT);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
        forFollower.forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), followerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), wolfUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.ENTER_WOLF_CHAT);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
    }

    @Test
    public void betrayerAndFollowerFailedToFindWolf() {
        ActivatedSkillInfo betrayerFoundWolf = betrayerUser.activateSkill(predictorUser, SkillType.ENTER_WOLF_CHAT);
        ActivatedSkillInfo followerFoundWolf = followerUser.activateSkill(humanMouseUser, SkillType.ENTER_WOLF_CHAT);

        assertFalse(betrayerFoundWolf.getSkillCondition().isSuccess(betrayerUser, predictorUser, SkillType.ENTER_WOLF_CHAT));
        assertFalse(followerFoundWolf.getSkillCondition().isSuccess(followerUser, humanMouseUser, SkillType.ENTER_WOLF_CHAT));
        assertEquals(betrayerFoundWolf.getTarget(), predictorUser);
        assertEquals(followerFoundWolf.getTarget(), humanMouseUser);
        betrayerFoundWolf.getOnSkillFail().onSkillFail(betrayerUser, predictorUser, SkillType.ENTER_WOLF_CHAT);
        followerFoundWolf.getOnSkillFail().onSkillFail(followerUser, humanMouseUser, SkillType.ENTER_WOLF_CHAT);
        List<SkillNotificationBody> skillNotifications = gameManager.getSkillNotifications();
        List<SkillNotificationBody> forBetrayer = skillNotifications.stream()
                .filter(e -> e.getReceiverUserId() == betrayerUser.getID()).toList();
        List<SkillNotificationBody> forFollower = skillNotifications.stream()
                .filter(e -> e.getReceiverUserId() == followerUser.getID()).toList();
        assertEquals(forBetrayer.size(), 1);
        assertEquals(forFollower.size(), 1);
        forBetrayer.forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), betrayerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), predictorUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE);
                    assertEquals(note.getSkillTargetCharacterCode(), predictorUser.getCharacter().getCharacterCode());
                });
        forFollower.forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), followerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), humanMouseUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE);
                    assertEquals(note.getSkillTargetCharacterCode(), humanMouseUser.getCharacter().getCharacterCode());
                });
    }

    @Test
    public void wolfExterminateOnce() {
        ActivatedSkillInfo result = wolfUser.activateSkill(citizenUser, SkillType.EXTERMINATE);
        assertTrue(result.getSkillCondition().isSuccess(wolfUser, citizenUser, SkillType.EXTERMINATE));
        result.getOnSkillSucceed().onSkillSucceed(result.getActivator(), result.getTarget(), result.getSkillType());
        assertEquals(1, gameManager.getSkillNotifications().size());
        assertEquals(citizenUser.getID(), gameManager.getSkillNotifications().get(0).getSkillTargetUserId());
    }

    @Test
    public void wolfExterminateMoreThanOnce() {
        ActivatedSkillInfo result1 = wolfUser.activateSkill(citizenUser, SkillType.EXTERMINATE);
        assertEquals(SkillType.EXTERMINATE, result1.getSkillType());
        ActivatedSkillInfo result2 = wolfUser.activateSkill(citizenUser, SkillType.EXTERMINATE);
        assertEquals(SkillType.NONE, result2.getSkillType());
        ActivatedSkillInfo result3 = wolfUser.activateSkill(citizenUser, SkillType.EXTERMINATE);
        assertEquals(SkillType.NONE, result3.getSkillType());
    }
}

package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


    UserRepository userRepository;

    @BeforeEach
    public void initialize() {
        wolf = new Wolf();
        betrayer = new Betrayer();
        follower = new Follower();
        predictor = new Predictor();
        guard = new Guard();
        mediumship = new Mediumship();
        detective = new Detective();
        successor = new Successor();
        nobility = new Nobility();
        soldier = new Soldier();
        secretSociety = new SecretSociety();
        templar = new Templar();
        citizen = new Citizen();
        murderer = new Murderer();
        humanMouse = new HumanMouse();
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
        SkillEffect betrayerFoundWolf = betrayerUser.activateSkill(wolfUser, SkillType.ENTER_WOLF_CHAT);
        SkillEffect followerFoundWolf = followerUser.activateSkill(wolfUser, SkillType.ENTER_WOLF_CHAT);

        assertTrue(betrayerFoundWolf.getSkillCondition().isSuccess(betrayerUser, wolfUser, SkillType.ENTER_WOLF_CHAT));
        assertTrue(followerFoundWolf.getSkillCondition().isSuccess(followerUser, wolfUser, SkillType.ENTER_WOLF_CHAT));
        assertEquals(betrayerFoundWolf.getActivator(), betrayerUser);
        assertEquals(followerFoundWolf.getActivator(), followerUser);
        assertEquals(betrayerFoundWolf.getTarget(), wolfUser);
        assertEquals(followerFoundWolf.getActivator(), followerUser);
        betrayerFoundWolf.getOnSkillSucceed().onSkillSucceed(betrayerUser, wolfUser, SkillType.ENTER_WOLF_CHAT);
        followerFoundWolf.getOnSkillSucceed().onSkillSucceed(followerUser, wolfUser, SkillType.ENTER_WOLF_CHAT);
        assertEquals(wolfUser.getNotificationAfterNight().size(), 2);
        assertEquals(betrayerUser.getNotificationAfterNight().size(), 1);
        assertEquals(followerUser.getNotificationAfterNight().size(), 1);
        wolfUser.getNotificationAfterNight()
                .forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), wolfUser.getID());
                    assertTrue(note.getMessage().matches(".+ entered the wolf chat"));
                    System.out.println(note.getSkillTargetUserId());
                    assertEquals(note.getSkillTargetUserId(), wolfUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.NOTIFY);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
        betrayerUser.getNotificationAfterNight()
                .forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), betrayerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), wolfUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.ENTER_WOLF_CHAT);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
        followerUser.getNotificationAfterNight()
                .forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), followerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), wolfUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.ENTER_WOLF_CHAT);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
    }

    @Test
    public void betrayerAndFollowerFailedToFindWolf() {
        SkillEffect betrayerFoundWolf = betrayerUser.activateSkill(predictorUser, SkillType.ENTER_WOLF_CHAT);
        SkillEffect followerFoundWolf = followerUser.activateSkill(humanMouseUser, SkillType.ENTER_WOLF_CHAT);

        assertFalse(betrayerFoundWolf.getSkillCondition().isSuccess(betrayerUser, predictorUser, SkillType.ENTER_WOLF_CHAT));
        assertFalse(followerFoundWolf.getSkillCondition().isSuccess(followerUser, humanMouseUser, SkillType.ENTER_WOLF_CHAT));
        assertEquals(betrayerFoundWolf.getTarget(), predictorUser);
        assertEquals(followerFoundWolf.getTarget(), humanMouseUser);
        betrayerFoundWolf.getOnSkillFail().onSkillFail(betrayerUser, predictorUser, SkillType.ENTER_WOLF_CHAT);
        followerFoundWolf.getOnSkillFail().onSkillFail(followerUser, humanMouseUser, SkillType.ENTER_WOLF_CHAT);
        assertEquals(betrayerUser.getNotificationAfterNight().size(), 1);
        assertEquals(followerUser.getNotificationAfterNight().size(), 1);
        betrayerUser.getNotificationAfterNight()
                .forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), betrayerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), predictorUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE);
                    assertEquals(note.getSkillTargetCharacterCode(), null);
                });
        followerUser.getNotificationAfterNight()
                .forEach((note) -> {
                    assertEquals(note.getReceiverUserId(), followerUser.getID());
                    assertTrue(note.getMessage() == null);
                    assertEquals(note.getSkillTargetUserId(), humanMouseUser.getID());
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE);
                    assertEquals(note.getSkillTargetCharacterCode(), null);
                });
    }
}

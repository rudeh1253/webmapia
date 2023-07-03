package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.SkillManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    SkillManager skillManager = new SkillManager();

    UserRepository userRepository;

    @BeforeEach
    public void initialize() {
        skillManager.clearSkillEffects();
        wolf = new Wolf(skillManager);
        betrayer = new Betrayer(skillManager);
        follower = new Follower(skillManager);
        predictor = new Predictor(skillManager);
        guard = new Guard(skillManager);
        mediumship = new Mediumship(skillManager);
        detective = new Detective(skillManager);
        successor = new Successor(skillManager);
        nobility = new Nobility(skillManager);
        soldier = new Soldier(skillManager);
        secretSociety = new SecretSociety(skillManager);
        templar = new Templar(skillManager);
        citizen = new Citizen(skillManager);
        murderer = new Murderer(skillManager);
        humanMouse = new HumanMouse(skillManager);
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
        assertEquals(4, skillManager.getSkillEffects().size());
        List<SkillEffect> forWolf = skillManager.getSkillEffects().stream()
                .filter(e -> Objects.equals(e.getReceiverUser(), wolfUser))
                .toList();
        List<SkillEffect> forBetrayer = skillManager.getSkillEffects().stream()
                .filter(e -> Objects.equals(e.getReceiverUser(), betrayerUser))
                .toList();
        List<SkillEffect> forFollower = skillManager.getSkillEffects().stream()
                .filter(e -> e.getReceiverUser() == followerUser)
                .toList();
        assertEquals(2, forWolf.size());
        assertEquals(1, forBetrayer.size());
        assertEquals(1, forFollower.size());
        forWolf.forEach((note) -> {
                    assertEquals(note.getReceiverUser(), wolfUser);
                    assertTrue(note.getMessage().matches(".+ entered the wolf chat"));
                    System.out.println(note.getSkillTargetUser());
                    assertEquals(note.getSkillTargetUser(), wolfUser);
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.NOTIFY);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
        forBetrayer.forEach((note) -> {
                    assertEquals(note.getReceiverUser(), betrayerUser);
                    assertNull(note.getMessage());
                    assertEquals(note.getSkillTargetUser(), wolfUser);
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.ENTER_WOLF_CHAT);
                    assertEquals(note.getSkillTargetCharacterCode(), CharacterCode.WOLF);
                });
        forFollower.forEach((note) -> {
                    assertEquals(note.getReceiverUser(), followerUser);
                    assertNull(note.getMessage());
                    assertEquals(note.getSkillTargetUser(), wolfUser);
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
        List<SkillEffect> skillNotifications = skillManager.getSkillEffects();
        List<SkillEffect> forBetrayer = skillNotifications.stream()
                .filter(e -> e.getReceiverUser() == betrayerUser).toList();
        List<SkillEffect> forFollower = skillNotifications.stream()
                .filter(e -> e.getReceiverUser() == followerUser).toList();
        assertEquals(forBetrayer.size(), 1);
        assertEquals(forFollower.size(), 1);
        forBetrayer.forEach((note) -> {
                    assertEquals(note.getReceiverUser(), betrayerUser);
                    assertNull(note.getMessage());
                    assertEquals(note.getSkillTargetUser(), predictorUser);
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE);
                    assertEquals(note.getSkillTargetCharacterCode(), predictorUser.getCharacter().getCharacterCode());
                });
        forFollower.forEach((note) -> {
                    assertEquals(note.getReceiverUser(), followerUser);
                    assertNull(note.getMessage());
                    assertEquals(note.getSkillTargetUser(), humanMouseUser);
                    assertEquals(note.getCharacterEffectAfterNightType(), CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE);
                    assertEquals(note.getSkillTargetCharacterCode(), humanMouseUser.getCharacter().getCharacterCode());
                });
    }

    @Test
    public void wolfExterminateOnce() {
        ActivatedSkillInfo result = wolfUser.activateSkill(citizenUser, SkillType.EXTERMINATE);
        assertTrue(result.getSkillCondition().isSuccess(wolfUser, citizenUser, SkillType.EXTERMINATE));
        result.getOnSkillSucceed().onSkillSucceed(result.getActivator(), result.getTarget(), result.getSkillType());
        assertEquals(1, skillManager.getSkillEffects().size());
        assertEquals(citizenUser, skillManager.getSkillEffects().get(0).getSkillTargetUser());
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

    @Test
    public void predictorInvestigation() {
        List<ActivatedSkillInfo> activatedSkills = new ArrayList<>();
        activatedSkills.add(predictorUser.activateSkill(mediumshipUser, SkillType.INVESTIGATE_ALIVE_CHARACTER));
        activatedSkills.add(predictorUser.activateSkill(guardUser, SkillType.INVESTIGATE_ALIVE_CHARACTER));
        activatedSkills.add(predictorUser.activateSkill(templarUser, SkillType.INVESTIGATE_ALIVE_CHARACTER));
        activatedSkills.forEach(e -> {
            if (e.getSkillCondition().isSuccess(e.getActivator(), e.getTarget(), e.getSkillType())) {
                e.getOnSkillSucceed().onSkillSucceed(e.getActivator(), e.getTarget(), e.getSkillType());
            } else {
                e.getOnSkillFail().onSkillFail(e.getActivator(), e.getTarget(), e.getSkillType());
            }
        });
        List<SkillEffect> skillEffects = skillManager.getSkillEffects();

        assertEquals(3, skillManager.getSkillEffects().size());
        skillEffects.forEach(e -> assertEquals(CharacterEffectAfterNightType.INVESTIGATE, e.getCharacterEffectAfterNightType()));
        assertEquals(CharacterCode.MEDIUMSHIP, skillEffects.get(0).getSkillTargetCharacterCode());
        assertEquals(CharacterCode.GUARD, skillEffects.get(1).getSkillTargetCharacterCode());
        assertEquals(CharacterCode.GOOD_MAN, skillEffects.get(2).getSkillTargetCharacterCode());
    }

    @Test
    public void predictorKillHumanMouse() {
        ActivatedSkillInfo activatedSkillInfo = predictorUser.activateSkill(humanMouseUser, SkillType.INVESTIGATE_ALIVE_CHARACTER);
        if (activatedSkillInfo.getSkillCondition().isSuccess(predictorUser, humanMouseUser, SkillType.INVESTIGATE_ALIVE_CHARACTER)) {
            activatedSkillInfo.getOnSkillSucceed().onSkillSucceed(predictorUser, humanMouseUser, SkillType.INVESTIGATE_ALIVE_CHARACTER);
        }

        SkillEffect skillEffect = skillManager.getSkillEffects().get(0);
        assertEquals(CharacterCode.HUMAN_MOUSE, skillEffect.getSkillTargetCharacterCode());
        assertEquals(CharacterEffectAfterNightType.KILL, skillEffect.getCharacterEffectAfterNightType());
    }

    @Test
    public void guardSucceedToGuard_KillFirst() {
        wolfUser.activateSkill(citizenUser, SkillType.KILL)
                .getOnSkillSucceed()
                .onSkillSucceed(wolfUser, citizenUser, SkillType.KILL);

        ActivatedSkillInfo guardEffect = guardUser.activateSkill(citizenUser, SkillType.GUARD);

        assertTrue(guardEffect.getSkillCondition().isSuccess(guardUser, citizenUser, SkillType.GUARD));
        guardEffect.getOnSkillSucceed().onSkillSucceed(guardUser, citizenUser, SkillType.GUARD);

        assertEquals(2, skillManager.getSkillEffects().size());
        assertEquals(CharacterEffectAfterNightType.GUARD, skillManager.getSkillEffects().get(1).getCharacterEffectAfterNightType());
    }

    @Test
    public void guardSucceedToGuard_GuardFirst() {
        wolfUser.activateSkill(citizenUser, SkillType.KILL)
                .getOnSkillSucceed()
                .onSkillSucceed(wolfUser, citizenUser, SkillType.KILL);

        ActivatedSkillInfo guardEffect = guardUser.activateSkill(citizenUser, SkillType.GUARD);

        assertTrue(guardEffect.getSkillCondition().isSuccess(guardUser, citizenUser, SkillType.GUARD));
        guardEffect.getOnSkillSucceed().onSkillSucceed(guardUser, citizenUser, SkillType.GUARD);

        assertEquals(2, skillManager.getSkillEffects().size());
        assertEquals(CharacterEffectAfterNightType.GUARD, skillManager.getSkillEffects().get(1).getCharacterEffectAfterNightType());
    }

    @Test
    public void guardFailToGuard() {
        wolfUser.activateSkill(citizenUser, SkillType.KILL)
                .getOnSkillSucceed()
                .onSkillSucceed(wolfUser, citizenUser, SkillType.KILL);

        ActivatedSkillInfo guardEffect = guardUser.activateSkill(mediumshipUser, SkillType.GUARD);

        assertFalse(guardEffect.getSkillCondition().isSuccess(guardEffect.getActivator(), guardEffect.getTarget(), SkillType.GUARD));
        guardEffect.getOnSkillFail()
                .onSkillFail(guardEffect.getActivator(), guardEffect.getTarget(), SkillType.GUARD);

        assertEquals(2, skillManager.getSkillEffects().size());
        assertEquals(CharacterEffectAfterNightType.FAIL_TO_GUARD,
                skillManager.getSkillEffects().get(1).getCharacterEffectAfterNightType());
    }

    @Test
    public void testMediumship() {
        citizenUser.setDead(true);
        wolfUser.setDead(true);
        predictorUser.setDead(true);
        guardUser.setDead(true);
        ActivatedSkillInfo investigateAlive = mediumshipUser.activateSkill(humanMouseUser, SkillType.INVESTIGATE_DEAD_CHARACTER);
        ActivatedSkillInfo investigateWolf = mediumshipUser.activateSkill(wolfUser, SkillType.INVESTIGATE_DEAD_CHARACTER);
        ActivatedSkillInfo investigatePredictor = mediumshipUser.activateSkill(predictorUser, SkillType.INVESTIGATE_DEAD_CHARACTER);
        ActivatedSkillInfo investigateGuard = mediumshipUser.activateSkill(guardUser, SkillType.INVESTIGATE_DEAD_CHARACTER);
        ActivatedSkillInfo investigateOther = mediumshipUser.activateSkill(citizenUser, SkillType.INVESTIGATE_DEAD_CHARACTER);

        assertFalse(investigateAlive.getSkillCondition()
                .isSuccess(investigateAlive.getActivator(), investigateAlive.getTarget(), investigateAlive.getSkillType()));
        assertTrue(investigateWolf.getSkillCondition()
                .isSuccess(investigateWolf.getActivator(), investigateWolf.getTarget(), investigateWolf.getSkillType()));
        assertTrue(investigatePredictor.getSkillCondition()
                .isSuccess(investigatePredictor.getActivator(), investigatePredictor.getTarget(), investigatePredictor.getSkillType()));
        assertTrue(investigateGuard.getSkillCondition()
                .isSuccess(investigateGuard.getActivator(), investigateGuard.getTarget(), investigateGuard.getSkillType()));
        assertTrue(investigateOther.getSkillCondition()
                .isSuccess(investigateOther.getActivator(), investigateOther.getTarget(), investigateOther.getSkillType()));

        investigateWolf
                .getOnSkillSucceed()
                .onSkillSucceed(investigateWolf.getActivator(), investigateWolf.getTarget(), investigateWolf.getSkillType());
        investigateGuard
                .getOnSkillSucceed()
                .onSkillSucceed(investigateGuard.getActivator(), investigateGuard.getTarget(), investigateGuard.getSkillType());
        investigatePredictor
                .getOnSkillSucceed()
                .onSkillSucceed(investigatePredictor.getActivator(), investigatePredictor.getTarget(), investigatePredictor.getSkillType());
        investigateOther
                .getOnSkillSucceed()
                .onSkillSucceed(investigateOther.getActivator(), investigateOther.getTarget(), investigateOther.getSkillType());
        skillManager.getSkillEffects().forEach(e -> {
            CharacterCode targetCharacterCode = userRepository.findById(e.getSkillTargetUser().getID())
                    .get()
                    .getCharacter()
                    .getCharacterCode();
            switch (targetCharacterCode) {
                case WOLF:
                    assertEquals(CharacterCode.WOLF, e.getSkillTargetCharacterCode());
                    break;
                case GUARD:
                    assertEquals(CharacterCode.GUARD, e.getSkillTargetCharacterCode());
                    break;
                case PREDICTOR:
                    assertEquals(CharacterCode.PREDICTOR, e.getSkillTargetCharacterCode());
                    break;
                case CITIZEN:
                    assertEquals(CharacterCode.GOOD_MAN, e.getSkillTargetCharacterCode());
                    break;
            }
        });
    }

    @Test
    public void detectiveFindBetrayer() {
        ActivatedSkillInfo activatedSkillInfo = detectiveUser.activateSkill(betrayerUser, SkillType.INVESTIGATE_ALIVE_CHARACTER);

        assertTrue(activatedSkillInfo
                .getSkillCondition()
                .isSuccess(activatedSkillInfo.getActivator(),
                        activatedSkillInfo.getTarget(),
                        activatedSkillInfo.getSkillType()));

        activatedSkillInfo.getOnSkillSucceed().onSkillSucceed(activatedSkillInfo.getActivator(),
                activatedSkillInfo.getTarget(),
                activatedSkillInfo.getSkillType());

        assertEquals(1, skillManager.getSkillEffects().size());
        assertEquals(detectiveUser, skillManager.getSkillEffects().get(0).getReceiverUser());
        assertEquals(CharacterCode.BETRAYER, skillManager.getSkillEffects().get(0).getSkillTargetCharacterCode());
    }

    @Test
    public void detectiveFindFollower() {
        ActivatedSkillInfo activatedSkillInfo = detectiveUser.activateSkill(followerUser, SkillType.INVESTIGATE_ALIVE_CHARACTER);

        assertTrue(activatedSkillInfo
                .getSkillCondition()
                .isSuccess(activatedSkillInfo.getActivator(),
                        activatedSkillInfo.getTarget(),
                        activatedSkillInfo.getSkillType()));

        activatedSkillInfo.getOnSkillSucceed().onSkillSucceed(activatedSkillInfo.getActivator(),
                activatedSkillInfo.getTarget(),
                activatedSkillInfo.getSkillType());

        assertEquals(1, skillManager.getSkillEffects().size());
        assertEquals(detectiveUser, skillManager.getSkillEffects().get(0).getReceiverUser());
        assertEquals(CharacterCode.FOLLOWER, skillManager.getSkillEffects().get(0).getSkillTargetCharacterCode());
    }

    @Test
    public void detectiveFailToFind() {
        ActivatedSkillInfo activatedSkillInfo = detectiveUser.activateSkill(wolfUser, SkillType.INVESTIGATE_ALIVE_CHARACTER);

        assertFalse(activatedSkillInfo
                .getSkillCondition()
                .isSuccess(activatedSkillInfo.getActivator(),
                        activatedSkillInfo.getTarget(),
                        activatedSkillInfo.getSkillType()));

        activatedSkillInfo.getOnSkillFail().onSkillFail(activatedSkillInfo.getActivator(),
                activatedSkillInfo.getTarget(),
                activatedSkillInfo.getSkillType());

        assertEquals(1, skillManager.getSkillEffects().size());
        assertEquals(detectiveUser, skillManager.getSkillEffects().get(0).getReceiverUser());
        assertEquals(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE, skillManager.getSkillEffects().get(0).getCharacterEffectAfterNightType());
    }

    @Test
    public void soldier() {
        ActivatedSkillInfo soldierSelfGuard = soldierUser.activateSkill(soldierUser, SkillType.GUARD);

        assertTrue(soldierSelfGuard
                .getSkillCondition()
                .isSuccess(soldierSelfGuard.getActivator(), soldierSelfGuard.getTarget(), soldierSelfGuard.getSkillType()));

        soldierSelfGuard
                .getOnSkillSucceed()
                .onSkillSucceed(soldierSelfGuard.getActivator(), soldierSelfGuard.getTarget(), soldierSelfGuard.getSkillType());

        assertEquals(1, skillManager.getSkillEffects().size());
        assertEquals(CharacterEffectAfterNightType.GUARD, skillManager.getSkillEffects().get(0).getCharacterEffectAfterNightType());

        ActivatedSkillInfo soldierSelfGuard2 = soldierUser.activateSkill(soldierUser, SkillType.GUARD);

        assertFalse(soldierSelfGuard2
                .getSkillCondition()
                .isSuccess(soldierSelfGuard2.getActivator(), soldierSelfGuard2.getTarget(), soldierSelfGuard2.getSkillType()));
    }

    @Test
    public void murderer() {
        ActivatedSkillInfo murder = murdererUser.activateSkill(citizenUser, SkillType.EXTERMINATE);

        assertTrue(murder.getSkillCondition().isSuccess(murder.getActivator(), murder.getTarget(), murder.getSkillType()));

        murder.getOnSkillSucceed().onSkillSucceed(murder.getActivator(), murder.getTarget(), murder.getSkillType());

        assertEquals(1, skillManager.getSkillEffects().size());
        assertEquals(CharacterEffectAfterNightType.EXTERMINATE, skillManager.getSkillEffects().get(0).getCharacterEffectAfterNightType());

        ActivatedSkillInfo murder2 = murdererUser.activateSkill(citizenUser, SkillType.EXTERMINATE);

        assertFalse(murder2.getSkillCondition().isSuccess(murder2.getActivator(), murder2.getTarget(), murder2.getSkillType()));
    }

    @Test
    public void murdererKillHumanMouse() {
        ActivatedSkillInfo murder = murdererUser.activateSkill(humanMouseUser, SkillType.EXTERMINATE);

        assertFalse(murder.getSkillCondition().isSuccess(murder.getActivator(), murder.getTarget(), murder.getSkillType()));
    }
}

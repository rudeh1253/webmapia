package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.notification.PublicNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import com.nsl.webmapia.game.service.PublicNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
    User follwerUser;
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
        follwerUser = new User(3L);
        follwerUser.setCharacter(follower);
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
        userRepository.save(follwerUser);
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
    public void betrayerAndFollowerFoundWolf() { // TODO make test strategy
        SkillEffect betrayerFoundWolf = betrayerUser.activateSkill(wolfUser, SkillType.ENTER_WOLF_CHAT);
        SkillEffect followerFoundWolf = follwerUser.activateSkill(wolfUser, SkillType.ENTER_WOLF_CHAT);

        assertThat(betrayerFoundWolf.getSkillCondition().isSuccess(betrayerUser, wolfUser, SkillType.ENTER_WOLF_CHAT))
                .isEqualTo(true);
        assertThat(followerFoundWolf.getSkillCondition().isSuccess(follwerUser, wolfUser, SkillType.ENTER_WOLF_CHAT))
                .isEqualTo(true);
    }
}

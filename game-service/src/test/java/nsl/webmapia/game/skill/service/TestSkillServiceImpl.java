package nsl.webmapia.game.skill.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static nsl.webmapia.game.character.domain.CharacterCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class TestSkillServiceImpl {

    @Autowired
    SkillServiceImpl skillService;

    @Autowired
    CharacterAssignmentRepository characterAssignmentRepository;

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    GameInstanceRepository gameInstanceRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    ActivatedSkillRepository activatedSkillRepository;

    int gameInstanceId;

    @BeforeEach
    void beforeEach() {
        GameRoom gameRoom = new GameRoom("sample-room", "host", LocalDateTime.now());
        this.gameRoomRepository.save(gameRoom);

        Participation participation1 = new Participation("host", gameRoom);
        Participation participation2 = new Participation("member1", gameRoom);
        Participation participation3 = new Participation("member2", gameRoom);
        Participation participation4 = new Participation("member3", gameRoom);
        Participation participation5 = new Participation("member4", gameRoom);
        Participation participation6 = new Participation("member5", gameRoom);
        Participation participation7 = new Participation("member6", gameRoom);
        Participation participation8 = new Participation("member7", gameRoom);

        this.participationRepository.save(participation1);
        this.participationRepository.save(participation2);
        this.participationRepository.save(participation3);
        this.participationRepository.save(participation4);
        this.participationRepository.save(participation5);
        this.participationRepository.save(participation6);
        this.participationRepository.save(participation7);
        this.participationRepository.save(participation8);

        GameInstance gameInstance = new GameInstance();
        gameInstance.setGameRoom(gameRoom);
        gameInstance.setRound(1);
        gameInstance.setStartTime(LocalDateTime.now());
        gameInstance.setGamePhase(GamePhase.START);

        this.gameInstanceRepository.save(gameInstance);

        this.gameInstanceId = gameInstance.getGameInstanceId();

        this.characterAssignmentRepository.save(generateCharacterAssignment("host", WOLF, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member1", FOLLOWER, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member2", DETECTIVE, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member3", CITIZEN, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member4", CITIZEN, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member5", CITIZEN, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member6", CITIZEN, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member7", CITIZEN, 1, gameInstance));
    }

    private CharacterAssignment generateCharacterAssignment(String memberId,
                                                            CharacterCode characterCode,
                                                            int life,
                                                            GameInstance gameInstance) {
        CharacterAssignment characterAssignment = new CharacterAssignment();
        characterAssignment.setMemberId(memberId);
        characterAssignment.setCharacterCode(characterCode);
        characterAssignment.setLife(life);
        characterAssignment.setGameInstance(gameInstance);
        return characterAssignment;
    }

    @DisplayName("getAvailableSkills - wolf - first attemption")
    @Test
    void getAvailableSkills_wolf_noSkillUsed() {
        List<SkillType> wolfAvailable = this.skillService.getAvailableSkills(gameInstanceId, "host");

        assertThat(wolfAvailable).containsExactlyInAnyOrder(SkillType.BEHEAD, SkillType.KILL);
    }

    @DisplayName("getAvailableSkills - wolf - after activating BEHEAD")
    @Test
    void getAvailableSkills_wolf_afterUseBehead() {
        GameInstance gameInstance = this.gameInstanceRepository.findById(this.gameInstanceId).orElseThrow();
        CharacterAssignment characterAssignment = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "host").orElseThrow();

        ActivatedSkill behead = new ActivatedSkill();
        behead.setSkillType(SkillType.BEHEAD);
        behead.setGameInstance(gameInstance);
        behead.setRound(gameInstance.getRound());
        behead.setCharacterAssignment(characterAssignment);
        this.activatedSkillRepository.save(behead);

        List<SkillType> expectedNoBeheadHere = this.skillService.getAvailableSkills(this.gameInstanceId, "host");

        assertThat(expectedNoBeheadHere).containsExactly(SkillType.KILL);
    }
}
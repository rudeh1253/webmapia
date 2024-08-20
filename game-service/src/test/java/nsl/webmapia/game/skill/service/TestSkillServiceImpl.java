package nsl.webmapia.game.skill.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.skill.domain.SkillEffect;
import nsl.webmapia.game.skill.domain.SkillEffectType;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static nsl.webmapia.game.character.domain.CharacterCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

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
        gameInstance.setGamePhase(GamePhase.NIGHT);

        this.gameInstanceRepository.save(gameInstance);

        this.gameInstanceId = gameInstance.getGameInstanceId();

        this.characterAssignmentRepository.save(generateCharacterAssignment("host", WOLF, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member1", FOLLOWER, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member2", DETECTIVE, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment("member3", GUARD, 1, gameInstance));
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
        Map<SkillType, List<String>> wolfAvailable =
                this.skillService.getAvailableSkills(this.gameInstanceId, "host");

        log.info("wolfAvailable.content={}", wolfAvailable);

        assertThat(wolfAvailable.keySet()).containsExactlyInAnyOrder(SkillType.BEHEAD, SkillType.KILL);
    }

    @DisplayName("getAvailableSkills - wolf - after activating BEHEAD")
    @Test
    void getAvailableSkills_wolf_afterUseBehead() {
        GameInstance gameInstance = this.gameInstanceRepository.findById(this.gameInstanceId).orElseThrow();

        ActivatedSkill behead = new ActivatedSkill();
        behead.setSkillType(SkillType.BEHEAD);
        behead.setRound(gameInstance.getRound());
        behead.setActivator(this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(gameInstanceId, "host").get());
        behead.setTarget(this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(gameInstanceId, "member1").get());
        this.activatedSkillRepository.save(behead);

        Map<SkillType, List<String>> expectedNoBeheadHere =
                this.skillService.getAvailableSkills(this.gameInstanceId, "host");

        log.info("expectedNoBeheadHere.content={}", expectedNoBeheadHere);

        assertThat(expectedNoBeheadHere.keySet()).containsExactly(SkillType.KILL);
    }

    @DisplayName("activateSkill")
    @Test
    void activateSkill() {
        assertThatNoException()
                .isThrownBy(() -> this.skillService.activateSkill(this.gameInstanceId, "host", "member1", SkillType.KILL));
    }

    @DisplayName("After activate BEHEAD and getTitle available skills")
    @Test
    void activateSkill_then_getAvailableSkills() {
        Map<SkillType, List<String>> firstAvailable =
                this.skillService.getAvailableSkills(this.gameInstanceId, "host");
        assertThat(firstAvailable.keySet()).containsExactlyInAnyOrder(SkillType.KILL, SkillType.BEHEAD);
        this.skillService.activateSkill(this.gameInstanceId, "host", "member1", SkillType.BEHEAD);

        Map<SkillType, List<String>> result =
                this.skillService.getAvailableSkills(this.gameInstanceId, "host");

        assertThat(result.keySet()).doesNotContain(SkillType.BEHEAD);
        assertThat(result.keySet()).containsExactly(SkillType.KILL);
    }

    @DisplayName("processSkills - wolf kills CITIZEN")
    @Test
    void processSkills1() {
        CharacterAssignment member5 = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "member5").get();
        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isFalse();

        this.skillService.activateSkill(this.gameInstanceId, "host", "member5", SkillType.KILL);

        List<SkillEffect> skillEffects = this.skillService.processSkills(this.gameInstanceId);
        log.info("skillEffects={}", skillEffects);

        assertThat(skillEffects.size()).isEqualTo(1);

        SkillEffect killEffect = skillEffects.get(0);

        assertThat(killEffect.getActivatorId()).isEqualTo("host");
        assertThat(killEffect.getReceiverIds().size()).isGreaterThan(1);
        assertThat(killEffect.getTargetId()).isEqualTo("member5");
        assertThat(killEffect.getType()).isEqualTo(SkillEffectType.KILL_SUCCESS);

        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isTrue();
    }

    @DisplayName("processSkill - wolf attempts to kill, but guarded")
    @Test
    void processSkill2() {
        CharacterAssignment member5 = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "member5").get();
        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isFalse();

        this.skillService.activateSkill(this.gameInstanceId, "host", "member5", SkillType.KILL);
        this.skillService.activateSkill(this.gameInstanceId, "member3", "member5", SkillType.GUARD);

        List<SkillEffect> skillEffects = this.skillService.processSkills(this.gameInstanceId);
        log.info("skillEffects={}", skillEffects);

        assertThat(skillEffects.size()).isEqualTo(2);

        SkillEffect killEffect = skillEffects.stream().filter((s) -> s.getType() == SkillEffectType.KILL_FAIL).findAny().get();
        SkillEffect guardEffect = skillEffects.stream().filter((s) -> s.getType() == SkillEffectType.GUARD_SUCCESS).findAny().get();

        assertThat(killEffect.getActivatorId()).isEqualTo("host");
        assertThat(killEffect.getReceiverIds().size()).isEqualTo(1);
        assertThat(killEffect.getTargetId()).isEqualTo("member5");
        assertThat(killEffect.getType()).isEqualTo(SkillEffectType.KILL_FAIL);

        assertThat(guardEffect.getActivatorId()).isEqualTo("member3");
        assertThat(guardEffect.getReceiverIds().size()).isEqualTo(1);
        assertThat(guardEffect.getReceiverIds().get(0)).isEqualTo("member3");
        assertThat(guardEffect.getTargetId()).isEqualTo("member5");
        assertThat(guardEffect.getType()).isEqualTo(SkillEffectType.GUARD_SUCCESS);

        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isFalse();
    }
}
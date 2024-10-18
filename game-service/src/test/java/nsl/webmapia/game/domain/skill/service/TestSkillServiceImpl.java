package nsl.webmapia.game.domain.skill.service;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.gameoperation.domain.GamePhase;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.domain.gameroom.entity.GameRoom;
import nsl.webmapia.game.domain.gameroom.entity.Participation;
import nsl.webmapia.game.domain.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.domain.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.domain.member.repository.MemberRepository;
import nsl.webmapia.game.domain.skill.domain.SkillEffect;
import nsl.webmapia.game.domain.skill.domain.SkillEffectType;
import nsl.webmapia.game.domain.skill.domain.SkillType;
import nsl.webmapia.game.domain.skill.entity.ActivatedSkill;
import nsl.webmapia.game.domain.skill.repository.ActivatedSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Autowired
    MemberRepository memberRepository;

    int gameInstanceId;

    @BeforeEach
    void beforeEach() {
        GameRoom gameRoom = new GameRoom("sample-room", LocalDateTime.now());
        this.gameRoomRepository.save(gameRoom);

        Member host = this.memberRepository.save(new Member("host", "hostnick"));
        Member member1 = this.memberRepository.save(new Member("member1", "nick1"));
        Member member2 = this.memberRepository.save(new Member("member2", "nick2"));
        Member member3 = this.memberRepository.save(new Member("member3", "nick3"));
        Member member4 = this.memberRepository.save(new Member("member4", "nick4"));
        Member member5 = this.memberRepository.save(new Member("member5", "nick5"));
        Member member6 = this.memberRepository.save(new Member("member6", "nick6"));
        Member member7 = this.memberRepository.save(new Member("member7", "nick7"));

        Participation participation1 = new Participation(host, gameRoom);
        Participation participation2 = new Participation(member1, gameRoom);
        Participation participation3 = new Participation(member2, gameRoom);
        Participation participation4 = new Participation(member3, gameRoom);
        Participation participation5 = new Participation(member4, gameRoom);
        Participation participation6 = new Participation(member5, gameRoom);
        Participation participation7 = new Participation(member6, gameRoom);
        Participation participation8 = new Participation(member7, gameRoom);

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

        this.characterAssignmentRepository.save(generateCharacterAssignment(host, WOLF, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment(member1, FOLLOWER, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment(member2, DETECTIVE, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment(member3, GUARD, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment(member4, CITIZEN, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment(member5, CITIZEN, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment(member6, CITIZEN, 1, gameInstance));
        this.characterAssignmentRepository.save(generateCharacterAssignment(member7, CITIZEN, 1, gameInstance));
    }

    private CharacterAssignment generateCharacterAssignment(Member member,
                                                            CharacterCode characterCode,
                                                            int life,
                                                            GameInstance gameInstance) {
        CharacterAssignment characterAssignment = new CharacterAssignment();
        characterAssignment.setMember(member);
        characterAssignment.setCharacterCode(characterCode);
        characterAssignment.setLife(life);
        characterAssignment.setGameInstance(gameInstance);
        return characterAssignment;
    }

    @DisplayName("getAvailableSkills - wolf - first attemption")
    @Test
    void getAvailableSkills_wolf_noSkillUsed() {
        CharacterAssignment characterAssignment = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "host").get();
        Map<SkillType, List<Integer>> wolfAvailable =
                this.skillService.getAvailableSkills(this.gameInstanceId, characterAssignment.getAssignmentId());

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

        CharacterAssignment characterAssignment = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "host").get();
        Map<SkillType, List<Integer>> expectedNoBeheadHere =
                this.skillService.getAvailableSkills(this.gameInstanceId, characterAssignment.getAssignmentId());

        log.info("expectedNoBeheadHere.content={}", expectedNoBeheadHere);

        assertThat(expectedNoBeheadHere.keySet()).containsExactly(SkillType.KILL);
    }

    @DisplayName("activateSkill")
    @Test
    void activateSkill() {
        CharacterAssignment host = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "host").get();
        CharacterAssignment member1 = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "member1").get();
        assertThatNoException()
                .isThrownBy(() ->
                        this.skillService.activateSkill(this.gameInstanceId, host.getAssignmentId(), member1.getAssignmentId(), SkillType.KILL));
    }

    @DisplayName("After activate BEHEAD and getTitle available skills")
    @Test
    void activateSkill_then_getAvailableSkills() {
        CharacterAssignment host = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "host").get();
        CharacterAssignment member1 = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "member1").get();
        Map<SkillType, List<Integer>> firstAvailable =
                this.skillService.getAvailableSkills(this.gameInstanceId, host.getAssignmentId());
        assertThat(firstAvailable.keySet()).containsExactlyInAnyOrder(SkillType.KILL, SkillType.BEHEAD);
        this.skillService.activateSkill(this.gameInstanceId, host.getAssignmentId(), member1.getAssignmentId(), SkillType.BEHEAD);

        Map<SkillType, List<Integer>> result =
                this.skillService.getAvailableSkills(this.gameInstanceId, host.getAssignmentId());

        assertThat(result.keySet()).doesNotContain(SkillType.BEHEAD);
        assertThat(result.keySet()).containsExactly(SkillType.KILL);
    }

    @DisplayName("processSkills - wolf kills CITIZEN")
    @Test
    void processSkills1() {
        CharacterAssignment host = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "host").get();
        CharacterAssignment member5 = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "member5").get();
        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isFalse();
        this.skillService.activateSkill(this.gameInstanceId, host.getAssignmentId(), member5.getAssignmentId(), SkillType.KILL);

        List<SkillEffect> skillEffects = this.skillService.processSkills(this.gameInstanceId);
        log.info("skillEffects={}", skillEffects);

        assertThat(skillEffects.size()).isEqualTo(1);

        SkillEffect killEffect = skillEffects.get(0);

        assertThat(killEffect.getActivatorCharacterAssignmentId()).isEqualTo(host.getAssignmentId());
        assertThat(killEffect.getReceiverCharacterAssignmentIds().size()).isGreaterThan(1);
        assertThat(killEffect.getTargetCharacterAssignmentId()).isEqualTo(member5.getAssignmentId());
        assertThat(killEffect.getType()).isEqualTo(SkillEffectType.KILL_SUCCESS);

        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isTrue();
    }

    @DisplayName("processSkill - wolf attempts to kill, but guarded")
    @Test
    void processSkill2() {
        CharacterAssignment host = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "host").get();
        CharacterAssignment member3 = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "member3").get();
        CharacterAssignment member5 = this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(this.gameInstanceId, "member5").get();
        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isFalse();

        this.skillService.activateSkill(this.gameInstanceId, host.getAssignmentId(), member5.getAssignmentId(), SkillType.KILL);
        this.skillService.activateSkill(this.gameInstanceId, member3.getAssignmentId(), member5.getAssignmentId(), SkillType.GUARD);

        List<SkillEffect> skillEffects = this.skillService.processSkills(this.gameInstanceId);
        log.info("skillEffects={}", skillEffects);

        assertThat(skillEffects.size()).isEqualTo(2);

        SkillEffect killEffect = skillEffects.stream().filter((s) -> s.getType() == SkillEffectType.KILL_FAIL).findAny().get();
        SkillEffect guardEffect = skillEffects.stream().filter((s) -> s.getType() == SkillEffectType.GUARD_SUCCESS).findAny().get();

        assertThat(killEffect.getActivatorCharacterAssignmentId()).isEqualTo(host.getAssignmentId());
        assertThat(killEffect.getReceiverCharacterAssignmentIds().size()).isEqualTo(1);
        assertThat(killEffect.getTargetCharacterAssignmentId()).isEqualTo(member5.getAssignmentId());
        assertThat(killEffect.getType()).isEqualTo(SkillEffectType.KILL_FAIL);

        assertThat(guardEffect.getActivatorCharacterAssignmentId()).isEqualTo(member3.getAssignmentId());
        assertThat(guardEffect.getReceiverCharacterAssignmentIds().size()).isEqualTo(1);
        assertThat(guardEffect.getReceiverCharacterAssignmentIds().get(0)).isEqualTo(member3.getAssignmentId());
        assertThat(guardEffect.getTargetCharacterAssignmentId()).isEqualTo(member5.getAssignmentId());
        assertThat(guardEffect.getType()).isEqualTo(SkillEffectType.GUARD_SUCCESS);

        log.info("member5.isDead()={}", member5.isDead());
        assertThat(member5.isDead()).isFalse();
    }
}
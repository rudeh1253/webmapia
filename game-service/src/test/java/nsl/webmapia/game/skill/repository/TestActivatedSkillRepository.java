package nsl.webmapia.game.skill.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.service.GameServiceImpl;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.member.entity.Member;
import nsl.webmapia.game.member.repository.MemberRepository;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static nsl.webmapia.game.character.domain.CharacterCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class TestActivatedSkillRepository {

    @Autowired
    GameServiceImpl gameService;

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    ActivatedSkillRepository activatedSkillRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    CharacterAssignmentRepository characterAssignmentRepository;

    @Autowired
    GameInstanceRepository gameInstanceRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("findByGameInstanceIdAndRound()")
    @Test
    void findByGameInstanceIdAndRound() {
        String[] sampleParticipantIds = {
                "sample-member01",
                "sample-member02",
                "sample-member03",
                "sample-member04",
                "sample-member05",
                "sample-member06",
                "sample-member07",
                "sample-member08",
                "sample-member09",
                "sample-member10",
                "sample-member11",
                "sample-member12",
                "sample-member13",
                "sample-member14"
        };

        CharacterCode[] characterAssignments = {
                WOLF,
                BETRAYER,
                FOLLOWER,
//                PREDICTOR,
                GUARD,
//                MEDIUMSHIP,
                DETECTIVE,
                SECRET_SOCIETY,
                NOBILITY,
                SOLDIER,
                TEMPLAR,
                CITIZEN,
                MURDERER,
                HUMAN_MOUSE,
                CITIZEN,
                CITIZEN
        };

        GameRoom sampleGameRoom = getSampleGameRoom();
        this.gameRoomRepository.save(sampleGameRoom);

        List<Member> sampleParticipants = Arrays.stream(sampleParticipantIds)
                .map((s) -> new Member(s, s + "nick"))
                .map(this.memberRepository::save)
                .toList();

        for (Member sampleParticipant : sampleParticipants) {
            this.participationRepository.save(new Participation(sampleParticipant, sampleGameRoom));
        }

        this.gameService.startGame(sampleGameRoom.getRoomId());
        GameInstance gameInstance = this.gameInstanceRepository.findAliveGameInstanceByGameRoomId(sampleGameRoom.getRoomId()).get();

        for (int i = 0; i < sampleParticipants.size(); i++) {
            this.characterAssignmentRepository.save(generateCharacterAssignment(
                    sampleParticipants.get(i), characterAssignments[i], gameInstance
            ));
        }

        ActivatedSkill activatedSkill = new ActivatedSkill();
        activatedSkill.setSkillType(SkillType.KILL);
        activatedSkill.setRound(1);
        activatedSkill.setActivator(
                this.characterAssignmentRepository.findByGameInstanceId(gameInstance.getGameInstanceId()).get(0)
        );

        this.activatedSkillRepository.save(activatedSkill);

        List<ActivatedSkill> result = this.activatedSkillRepository.findByGameInstanceIdAndRound(gameInstance.getGameInstanceId(), 1);

        log.info("result={}", result);

        assertThat(result.size()).isEqualTo(1);

        assertThat(result.get(0).getSkillType()).isEqualTo(SkillType.KILL);
        assertThat(result.get(0).getRound()).isEqualTo(1);
    }

    private GameRoom getSampleGameRoom() {
        GameRoom sampleGameRoom = new GameRoom();
        sampleGameRoom.setRoomName("sample-room");
        sampleGameRoom.setCreationTime(LocalDateTime.now());
        return sampleGameRoom;
    }

    private CharacterAssignment generateCharacterAssignment(Member member, CharacterCode characterCode, GameInstance gameInstance) {
        CharacterAssignment characterAssignment = new CharacterAssignment();
        characterAssignment.setMember(member);
        characterAssignment.setCharacterCode(characterCode);
        characterAssignment.setLife(characterCode == SOLDIER ? 2 : 1);
        characterAssignment.setGameInstance(gameInstance);
        return characterAssignment;
    }
}
package nsl.webmapia.game.domain.gameoperation.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.gameoperation.domain.GamePhase;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.gameroom.entity.GameRoom;
import nsl.webmapia.game.domain.gameroom.entity.Participation;
import nsl.webmapia.game.domain.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.domain.gameroom.repository.SpringDataJpaGameRoomRepository;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.domain.member.repository.MemberRepository;
import nsl.webmapia.game.domain.vote.entity.Vote;
import nsl.webmapia.game.domain.vote.repository.SpringDataJpaVoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@Slf4j
@SpringBootTest
@Transactional
class TestSpringDataJpaVoteRepository {

    @Autowired
    SpringDataJpaGameRoomRepository gameRoomRepository;

    @Autowired
    HibernateGameInstanceRepository gameInstanceRepository;

    @Autowired
    SpringDataJpaVoteRepository voteRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CharacterAssignmentRepository characterAssignmentRepository;

    @Autowired
    ParticipationRepository participationRepository;

    CharacterAssignment ca1;
    CharacterAssignment ca2;
    CharacterAssignment ca3;

    @DisplayName("Check if save() works without any exception")
    @Test
    void save() {
        GameInstance gameInstance = insertSampleGameRoomAndGameInstance();

        Vote vote = new Vote(
                1,
                this.ca1,
                this.ca2,
                1,
                gameInstance
        );
        assertThatNoException().isThrownBy(() -> this.voteRepository.save(vote));
    }

    @DisplayName("findByGameInstanceIdAndRound() - find correctly")
    @Test
    void findByGameInstanceAndRound() {
        GameInstance gameInstance = insertSampleGameRoomAndGameInstance();

        Vote vote1 = new Vote(
                1,
                this.ca1,
                this.ca2,
                1,
                gameInstance
        );
        this.voteRepository.save(vote1);

        Vote vote2 = new Vote(
                1,
                this.ca2,
                this.ca3,
                1,
                gameInstance
        );
        this.voteRepository.save(vote2);

        Set<Vote> result = this.voteRepository.findByGameInstanceIdAndRound(gameInstance.getGameInstanceId(), 1);

        result.forEach((v) -> log.info("v={}", v));

        assertThat(result.stream().map((v) -> v.getVoteId().getRound()).toList())
                .containsExactlyInAnyOrder(vote1.getVoteId().getRound(), vote2.getVoteId().getRound());
        assertThat(result.stream().map((v) -> v.getVoteId().getGameInstanceId()).toList())
                .containsExactlyInAnyOrder(vote1.getVoteId().getGameInstanceId(), vote2.getVoteId().getGameInstanceId());
        assertThat(result.stream().map((v) -> v.getVoteId().getVoter().getAssignmentId()).toList())
                .containsExactlyInAnyOrder(vote1.getVoteId().getVoter().getAssignmentId(), vote2.getVoteId().getVoter().getAssignmentId());
    }

    private GameInstance insertSampleGameRoomAndGameInstance() {
        Member member1 = new Member("member1", "nick1");
        Member member2 = new Member("member2", "nick2");
        Member member3 = new Member("member3", "nick3");
        this.memberRepository.save(member1);
        this.memberRepository.save(member2);
        this.memberRepository.save(member3);

        GameRoom gameRoom = new GameRoom(
                "sample-room",
                LocalDateTime.now()
        );
        this.gameRoomRepository.save(gameRoom);

        this.participationRepository.save(new Participation(member1, true, gameRoom));
        this.participationRepository.save(new Participation(member1, gameRoom));
        this.participationRepository.save(new Participation(member1, gameRoom));

        GameInstance gameInstance = GameInstance.builder()
                .round(1)
                .startTime(LocalDateTime.now())
                .gamePhase(GamePhase.START)
                .gameRoom(gameRoom)
                .build();
        this.gameInstanceRepository.save(gameInstance);

        this.ca1 = new CharacterAssignment(member1, CharacterCode.WOLF, gameInstance);
        this.characterAssignmentRepository.save(this.ca1);
        this.ca2 = new CharacterAssignment(member2, CharacterCode.MURDERER, gameInstance);
        this.characterAssignmentRepository.save(this.ca2);
        this.ca3 = new CharacterAssignment(member3, CharacterCode.CITIZEN, gameInstance);
        this.characterAssignmentRepository.save(this.ca3);
        return gameInstance;
    }
}
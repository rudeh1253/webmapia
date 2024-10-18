package nsl.webmapia.game.domain.gameoperation.service;

import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.domain.gameroom.entity.GameRoom;
import nsl.webmapia.game.domain.gameroom.entity.Participation;
import nsl.webmapia.game.domain.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.domain.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.domain.member.repository.MemberRepository;
import nsl.webmapia.game.domain.vote.dto.VoteDto;
import nsl.webmapia.game.domain.vote.dto.request.VoteRequestDto;
import nsl.webmapia.game.domain.vote.entity.Vote;
import nsl.webmapia.game.domain.vote.service.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TestVoteService {
    @Autowired
    VoteService voteService;

    @Autowired
    GameServiceImpl gameService;

    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    CharacterAssignmentRepository characterAssignmentRepository;

    @Autowired
    GameInstanceRepository gameInstanceRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("vote()")
    @Test
    void vote() {
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

        CharacterCode[] characterAssignmentsArr = {
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

        List<Member> participants = Arrays.stream(sampleParticipantIds).map((s) -> new Member(s, s + "nick"))
                .map(this.memberRepository::save)
                .toList();

        for (Member sampleParticipant : participants) {
            this.participationRepository.save(new Participation(sampleParticipant, sampleGameRoom));
        }

        this.gameService.startGame(sampleGameRoom.getRoomId());
        GameInstance gameInstance = this.gameInstanceRepository.findAliveGameInstanceByGameRoomId(sampleGameRoom.getRoomId()).get();

        List<CharacterAssignment> characterAssignments = new ArrayList<>(characterAssignmentsArr.length);
        for (int i = 0; i < participants.size(); i++) {
            CharacterAssignment characterAssignment = generateCharacterAssignment(
                    participants.get(i), characterAssignmentsArr[i], gameInstance
            );
            this.characterAssignmentRepository.save(characterAssignment);
            characterAssignments.add(characterAssignment);
        }

        List<VoteDto> voteDtoAccumulator = new ArrayList<>();
        voteDtoAccumulator.add(VoteDto.of(getVoteForGeneratingDto(characterAssignments.get(0), characterAssignments.get(1), gameInstance)));

        checkVote(
                characterAssignments.get(0).getAssignmentId(),
                characterAssignments.get(1).getAssignmentId(),
                voteDtoAccumulator,
                gameInstance.getGameInstanceId(),
                sampleParticipantIds
        );

        voteDtoAccumulator.add(VoteDto.of(getVoteForGeneratingDto(characterAssignments.get(1), characterAssignments.get(2), gameInstance)));

        checkVote(
                characterAssignments.get(1).getAssignmentId(),
                characterAssignments.get(2).getAssignmentId(),
                voteDtoAccumulator,
                gameInstance.getGameInstanceId(),
                sampleParticipantIds
        );

        voteDtoAccumulator.add(VoteDto.of(getVoteForGeneratingDto(characterAssignments.get(5), characterAssignments.get(4), gameInstance)));

        checkVote(
                characterAssignments.get(5).getAssignmentId(),
                characterAssignments.get(4).getAssignmentId(),
                voteDtoAccumulator,
                gameInstance.getGameInstanceId(),
                sampleParticipantIds
        );
    }

    private GameRoom getSampleGameRoom() {
        GameRoom sampleGameRoom = new GameRoom();
        sampleGameRoom.setRoomName("sample-room");
        sampleGameRoom.setCreationTime(LocalDateTime.now());
        return sampleGameRoom;
    }

    private VoteRequestDto generateVoteRequestDto(int gameInstanceId, Integer voterCharacterAssignmentId, Integer targetCharacterAssignmentId) {
        VoteRequestDto voteRequestDto = new VoteRequestDto();
        voteRequestDto.setGameInstanceId(gameInstanceId);
        voteRequestDto.setVoterId(voterCharacterAssignmentId);
        voteRequestDto.setTargetId(targetCharacterAssignmentId);
        return voteRequestDto;
    }

    private CharacterAssignment generateCharacterAssignment(Member member, CharacterCode characterCode, GameInstance gameInstance) {
        CharacterAssignment characterAssignment = new CharacterAssignment();
        characterAssignment.setMember(member);
        characterAssignment.setCharacterCode(characterCode);
        characterAssignment.setLife(characterCode == SOLDIER ? 2 : 1);
        characterAssignment.setGameInstance(gameInstance);
        return characterAssignment;
    }

    private void checkVote(Integer voterCharacterAssignmentId,
                           Integer targetCharacterAssignmentId,
                           List<VoteDto> expected,
                           int gameInstanceId,
                           String[] sampleParticipants) {
        List<VoteDto> votes =
                this.voteService.vote(generateVoteRequestDto(gameInstanceId, voterCharacterAssignmentId, targetCharacterAssignmentId));
        assertThat(votes.size()).isEqualTo(expected.size());
        assertThat(votes.stream().map(VoteDto::getVoterId).toArray(Integer[]::new))
                .containsExactlyInAnyOrder(expected.stream().map(VoteDto::getVoterId).toArray(Integer[]::new));
        assertThat(votes.stream().map(VoteDto::getTargetId).toArray(Integer[]::new))
                .containsExactlyInAnyOrder(expected.stream().map(VoteDto::getTargetId).toArray(Integer[]::new));
    }

    private Vote getVoteForGeneratingDto(CharacterAssignment voter, CharacterAssignment target, GameInstance gameInstance) {
        return new Vote(0, voter, target, 0, gameInstance);
    }
}
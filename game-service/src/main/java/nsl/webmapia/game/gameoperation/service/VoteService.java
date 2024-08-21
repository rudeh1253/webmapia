package nsl.webmapia.game.gameoperation.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.VoteResultResponseDto;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.entity.Vote;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {
    private final GameInstanceRepository gameInstanceRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private final CharacterDefinitionFactoryService characterDefinitionFactoryService;
    private final VoteRepository voteRepository;

    /**
     * Process a vote from a single member. The size of a single vote is determined by the character of
     * the member owns.
     *
     * @param voteRequestDto DTO contains data of voter id and target id
     * @return a list of vote executed in the current instance
     */
    List<VoteDto> vote(VoteRequestDto voteRequestDto) {
        GameInstance gameInstance = this.gameInstanceRepository.findById(voteRequestDto.getGameInstanceId())
                .orElseThrow(NoSuchElementException::new);
        // TODO: instead of IllegalArgumentException, more specific exception is needed.
        // The exception thrown here should be one that states no such Member of voterId isn't present.
        List<CharacterAssignment> characterAssignments = this.characterAssignmentRepository.findByGameInstanceId(gameInstance.getGameInstanceId());
        CharacterAssignment voterCharacterAssignment = characterAssignments.stream()
                .filter((ca) -> ca.getMemberId().equals(voteRequestDto.getVoterId()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        CharacterDefinitionService characterDefinitionService =
                this.characterDefinitionFactoryService.getCharacterDefinitionOfCharacterCode(voterCharacterAssignment.getCharacterCode());
        int voteCount = characterDefinitionService.getVoteCount();
        this.voteRepository.save(new Vote(
                gameInstance.getRound(),
                voteRequestDto.getVoterId(),
                voteRequestDto.getTargetId(),
                voteCount,
                gameInstance
        ));

        return this.voteRepository.findByGameInstanceIdAndRound(gameInstance.getGameInstanceId(), gameInstance.getRound())
                .stream()
                .map(VoteDto::of)
                .toList();
    }

    public VoteResultResponseDto processVote(int gameInstanceId) {
        GameInstance gameInstance = this.gameInstanceRepository.findById(gameInstanceId)
                .orElseThrow(IllegalArgumentException::new);
        Set<Vote> votesInRound =
                this.voteRepository.findByGameInstanceIdAndRound(gameInstanceId, gameInstance.getRound());

        Map<String, Integer> sum = new HashMap<>();
        for (Vote vote : votesInRound) {
            String targetId = vote.getTargetId();
            if (!sum.containsKey(targetId)) {
                sum.put(targetId, 0);
            }
            sum.put(targetId, sum.get(targetId) + vote.getVoteCount());
        }

        String maxMember = null;
        Integer max = 0;
        for (String memberId : sum.keySet()) {
            if (max < sum.get(memberId)) {
                maxMember = memberId;
                max = sum.get(memberId);
            }
        }
        return new VoteResultResponseDto(
                maxMember,
                votesInRound.stream().map(VoteDto::of).collect(Collectors.toList())
        );
    }
}

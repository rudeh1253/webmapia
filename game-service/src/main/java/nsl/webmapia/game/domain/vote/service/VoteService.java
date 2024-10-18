package nsl.webmapia.game.domain.vote.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionService;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.domain.vote.dto.VoteDto;
import nsl.webmapia.game.domain.vote.dto.request.VoteRequestDto;
import nsl.webmapia.game.domain.vote.dto.response.VoteResultResponseDto;
import nsl.webmapia.game.domain.vote.entity.Vote;
import nsl.webmapia.game.domain.vote.repository.VoteRepository;
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
    public List<VoteDto> vote(VoteRequestDto voteRequestDto) {
        GameInstance gameInstance = this.gameInstanceRepository.findById(voteRequestDto.getGameInstanceId())
                .orElseThrow(NoSuchElementException::new);
        // TODO: instead of IllegalArgumentException, more specific exception is needed.
        // The exception thrown here should be one that states no such Member of voterId isn't present.
        CharacterAssignment voterCharacterAssignment = this.characterAssignmentRepository.findById(voteRequestDto.getVoterId())
                .orElseThrow(NoSuchElementException::new);
        CharacterAssignment targetCharacterAssignment = this.characterAssignmentRepository.findById(voteRequestDto.getTargetId())
                .orElseThrow(NoSuchElementException::new);
        CharacterDefinitionService characterDefinitionService =
                this.characterDefinitionFactoryService.getCharacterDefinitionOfCharacterCode(voterCharacterAssignment.getCharacterCode());
        int voteCount = characterDefinitionService.getVoteCount();
        this.voteRepository.save(new Vote(
                gameInstance.getRound(),
                voterCharacterAssignment,
                targetCharacterAssignment,
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

        Map<Integer, Integer> sum = new HashMap<>();
        for (Vote vote : votesInRound) {
            Integer targetId = vote.getTarget().getAssignmentId();
            if (!sum.containsKey(targetId)) {
                sum.put(targetId, 0);
            }
            sum.put(targetId, sum.get(targetId) + vote.getVoteCount());
        }

        Integer maxCharacterAssignment = null;
        Integer max = 0;
        for (Integer characterAssignmentId : sum.keySet()) {
            if (max < sum.get(characterAssignmentId)) {
                maxCharacterAssignment = characterAssignmentId;
                max = sum.get(characterAssignmentId);
            }
        }
        return new VoteResultResponseDto(
                maxCharacterAssignment,
                votesInRound.stream().map(VoteDto::of).collect(Collectors.toList())
        );
    }
}

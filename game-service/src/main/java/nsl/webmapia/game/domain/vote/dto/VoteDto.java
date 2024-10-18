package nsl.webmapia.game.domain.vote.dto;

import lombok.*;
import nsl.webmapia.game.domain.vote.entity.Vote;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class VoteDto {
    private Integer voterId;
    private Integer targetId;
    private int voteCount;

    public static VoteDto of(Vote vote) {
        return VoteDto.builder()
                .voterId(vote.getVoteId().getVoter().getAssignmentId())
                .targetId(vote.getTarget().getAssignmentId())
                .voteCount(vote.getVoteCount())
                .build();
    }
}

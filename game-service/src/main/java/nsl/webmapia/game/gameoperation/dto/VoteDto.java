package nsl.webmapia.game.gameoperation.dto;

import lombok.*;
import nsl.webmapia.game.gameoperation.entity.Vote;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class VoteDto {
    private String voterId;
    private String targetId;
    private int voteCount;

    public static VoteDto of(Vote vote) {
        return VoteDto.builder()
                .voterId(vote.getVoteId().getVoterId())
                .targetId(vote.getTargetId())
                .voteCount(vote.getVoteCount())
                .build();
    }
}

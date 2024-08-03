package nsl.webmapia.game.gameoperation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nsl.webmapia.game.member.domain.Member;

/**
 * Class representing an instance of vote.
 */
@AllArgsConstructor
@Getter
@ToString
public class Vote {
    private Member voter;
    private Member target;
    private int voteCount;
}

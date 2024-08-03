package nsl.webmapia.game.gameoperation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Class representing an instance of vote.
 */
@AllArgsConstructor
@Getter
@ToString
public class Vote {
    private String voterId;
    private String targetId;
    private int voteCount;
}

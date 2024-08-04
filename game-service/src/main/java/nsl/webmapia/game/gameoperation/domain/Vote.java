package nsl.webmapia.game.gameoperation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Class representing an instance of vote.
 */
@AllArgsConstructor
@Getter
@ToString
public class Vote {
    private int gameRoomId;
    private LocalDateTime gameInstanceStartTime;
    private int round;
    private String voterId;
    private String targetId;
    private int voteCount;
}

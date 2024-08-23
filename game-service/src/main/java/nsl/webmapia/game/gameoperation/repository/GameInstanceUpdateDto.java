package nsl.webmapia.game.gameoperation.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import nsl.webmapia.game.gameoperation.domain.GamePhase;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class GameInstanceUpdateDto {
    private final int gameInstanceId;
    private final Integer round;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final GamePhase gamePhase;
    private final LocalDateTime phaseEndTime;
}

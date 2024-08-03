package nsl.webmapia.game.gameoperation.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nsl.webmapia.game.gameroom.domain.GameRoom;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameInstance {
    private GameRoom gameRoom;
    private int round;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private GamePhase gamePhase;
}

package nsl.webmapia.game.gameoperation.domain;

import nsl.webmapia.game.gameroom.domain.GameRoom;

import java.time.LocalDateTime;

public class GameInstance {
    private GameRoom gameRoom;
    private int round;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private GamePhase gamePhase;
}

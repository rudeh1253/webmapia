package nsl.webmapia.game.gameoperation.dto;

import lombok.*;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.entity.GameInstance;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class GameInstanceDto {
    private Integer gameInstanceId;
    private int round;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private GamePhase gamePhase;

    public static GameInstanceDto of(GameInstance gameInstance) {
        return GameInstanceDto.builder()
                .gameInstanceId(gameInstance.getGameInstanceId())
                .round(gameInstance.getRound())
                .startTime(gameInstance.getStartTime())
                .endTime(gameInstance.getEndTime())
                .gamePhase(gameInstance.getGamePhase())
                .build();
    }
}

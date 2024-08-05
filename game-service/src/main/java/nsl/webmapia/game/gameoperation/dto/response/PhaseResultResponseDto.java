package nsl.webmapia.game.gameoperation.dto.response;

import lombok.*;
import nsl.webmapia.game.gameoperation.domain.GamePhase;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class PhaseResultResponseDto {
    private boolean ended;
    private GamePhase currentPhase;
    private GamePhase nextPhase;
    private Object content;
}

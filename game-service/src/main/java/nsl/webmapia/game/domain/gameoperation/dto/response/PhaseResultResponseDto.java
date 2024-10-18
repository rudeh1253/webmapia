package nsl.webmapia.game.domain.gameoperation.dto.response;

import lombok.*;
import nsl.webmapia.game.domain.gameoperation.domain.GamePhase;

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

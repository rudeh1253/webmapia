package nsl.webmapia.game.domain.gameroom.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ParticipationRequestDto {
    private String newParticipantId;
}

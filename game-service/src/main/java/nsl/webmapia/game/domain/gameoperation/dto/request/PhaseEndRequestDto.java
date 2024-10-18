package nsl.webmapia.game.domain.gameoperation.dto.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class PhaseEndRequestDto {
    private Integer gameInstanceId;
    private String requesterId;
}

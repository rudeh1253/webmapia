package nsl.webmapia.game.domain.gameoperation.dto.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class GameStartRequestDto {
    private Integer gameRoomId;
}

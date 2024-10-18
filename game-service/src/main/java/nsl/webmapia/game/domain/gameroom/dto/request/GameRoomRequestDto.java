package nsl.webmapia.game.domain.gameroom.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomRequestDto {
    private Integer page;
    private Integer pageSize;
    private String roomName;
}

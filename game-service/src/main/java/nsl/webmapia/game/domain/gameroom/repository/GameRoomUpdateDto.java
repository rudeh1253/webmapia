package nsl.webmapia.game.domain.gameroom.repository;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomUpdateDto {
    private Integer roomId;
    private String roomName;
    private String hostMemberId;
}

package nsl.webmapia.game.domain.gameroom.dto.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomCreationRequestDto {
    private String roomName;
    private String creatorId;
}

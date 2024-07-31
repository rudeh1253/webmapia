package nsl.webmapia.game.gameroom.dto.response;

import lombok.*;
import nsl.webmapia.game.gameroom.domain.GameRoom;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomCreationResponseDto {
    private int roomId;
    private String roomName;
    private String hostMemberId;
    private LocalDateTime creationTime;

    public static GameRoomCreationResponseDto of(GameRoom newGameRoom) {
        return GameRoomCreationResponseDto.builder()
                .roomId(newGameRoom.getRoomId())
                .roomName(newGameRoom.getRoomName())
                .hostMemberId(newGameRoom.getHostMember().getMemberId())
                .creationTime(newGameRoom.getCreationTime())
                .build();
    }
}

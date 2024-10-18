package nsl.webmapia.game.gameroom.dto.response;

import lombok.*;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;

import java.time.LocalDateTime;

@NoArgsConstructor
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
                .creationTime(newGameRoom.getCreationTime())
                .hostMemberId(newGameRoom.getParticipationList().stream().filter(Participation::isHost).findAny().orElseThrow().getParticipant().getMemberId()) // TODO
                .build();
    }
}

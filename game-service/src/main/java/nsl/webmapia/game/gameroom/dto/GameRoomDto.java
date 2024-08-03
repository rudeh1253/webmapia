package nsl.webmapia.game.gameroom.dto;

import lombok.*;
import nsl.webmapia.game.gameroom.domain.GameRoom;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomDto {
    private int roomId;
    private String roomName;
    private String memberId;
    private LocalDateTime creationTime;
    private List<String> participantIds;

    public static GameRoomDto of(GameRoom domain) {
        return GameRoomDto.builder()
                .roomId(domain.getRoomId())
                .roomName(domain.getRoomName())
                .memberId(domain.getHostMemberId())
                .creationTime(domain.getCreationTime())
                .participantIds(domain.getParticipantIds())
                .build();
    }
}

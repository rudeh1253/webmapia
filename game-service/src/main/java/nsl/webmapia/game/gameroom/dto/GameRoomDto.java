package nsl.webmapia.game.gameroom.dto;

import lombok.*;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomDto {
    private Integer roomId;
    private String roomName;
    private String hostMemberId;
    private LocalDateTime creationTime;
    private List<String> participantIds;

    public static GameRoomDto of(GameRoom domain) {
        return GameRoomDto.builder()
                .roomId(domain.getRoomId())
                .roomName(domain.getRoomName())
                .hostMemberId(domain.getHostMemberId())
                .creationTime(domain.getCreationTime())
                .participantIds(domain.getParticipationList().stream().map(Participation::getParticipantId).toList())
                .build();
    }
}

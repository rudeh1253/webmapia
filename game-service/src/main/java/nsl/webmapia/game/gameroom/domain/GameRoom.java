package nsl.webmapia.game.gameroom.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameRoom {
    private Integer roomId;
    private String roomName;
    private String hostMemberId;
    private LocalDateTime creationTime;
    private List<String> participantIds;

    public GameRoom(String roomName,
                    String hostMemberId,
                    LocalDateTime creationTime,
                    List<String> participantIds) {
        this.roomName = roomName;
        this.hostMemberId = hostMemberId;
        this.creationTime = creationTime;
        this.participantIds = participantIds;
    }
}

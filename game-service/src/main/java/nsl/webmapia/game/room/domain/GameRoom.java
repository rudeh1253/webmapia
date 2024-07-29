package nsl.webmapia.game.room.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameRoom {
    private Integer roomId;
    private String roomName;
    private LocalDateTime creationTime;
}

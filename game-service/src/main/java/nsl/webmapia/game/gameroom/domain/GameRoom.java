package nsl.webmapia.game.gameroom.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nsl.webmapia.game.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameRoom {
    private Integer roomId;
    private String roomName;
    private Member hostMember;
    private LocalDateTime creationTime;
    private List<Member> participants;
}

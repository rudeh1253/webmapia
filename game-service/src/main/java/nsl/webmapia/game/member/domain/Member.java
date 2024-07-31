package nsl.webmapia.game.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nsl.webmapia.game.gameroom.domain.GameRoom;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Member {
    private String memberId;
    private String nickname;
    private GameRoom gameParticipation;

    public Member(String memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}

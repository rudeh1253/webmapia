package nsl.webmapia.game.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nsl.webmapia.game.gameroom.entity.GameRoom;

/**
 * This is a domain class representing a member.
 * This is deprecated. Member is managed by another microservice, and it is fetched on network which is costly.
 * Member data is fetched only on demand, and the data should be wrapped with DTO object.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Deprecated
public class Member {
    private String memberId;
    private String nickname;
    private GameRoom gameParticipation;

    public Member(String memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}

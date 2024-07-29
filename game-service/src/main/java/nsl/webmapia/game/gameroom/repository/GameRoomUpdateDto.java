package nsl.webmapia.game.gameroom.repository;

import lombok.*;
import nsl.webmapia.game.member.domain.Member;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomUpdateDto {
    private Integer roomId;
    private String roomName;
    private Member hostMember;
}

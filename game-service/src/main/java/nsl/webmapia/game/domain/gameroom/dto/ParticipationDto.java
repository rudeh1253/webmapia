package nsl.webmapia.game.domain.gameroom.dto;

import lombok.*;
import nsl.webmapia.game.domain.member.dto.MemberDto;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class ParticipationDto {
    private Integer participationId;
    private MemberDto participant;
    private boolean host;
    private boolean disconnected;
    private Integer gameRoomId;
}

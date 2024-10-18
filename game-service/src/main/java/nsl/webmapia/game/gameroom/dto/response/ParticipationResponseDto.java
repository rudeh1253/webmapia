package nsl.webmapia.game.gameroom.dto.response;

import lombok.*;
import nsl.webmapia.game.member.dto.MemberDto;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class ParticipationResponseDto {
    private int gameRoomId;
    private MemberDto newParticipant;
    private List<MemberDto> participants;
}

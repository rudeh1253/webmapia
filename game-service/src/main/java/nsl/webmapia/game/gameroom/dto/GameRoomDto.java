package nsl.webmapia.game.gameroom.dto;

import lombok.*;
import nsl.webmapia.game.gameroom.domain.GameRoom;
import nsl.webmapia.game.member.dto.MemberDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomDto {
    private int roomId;
    private String roomName;
    private String memberId;
    private LocalDateTime creationTime;
    private List<MemberDto> participants;

    public static GameRoomDto of(GameRoom domain) {
        return GameRoomDto.builder()
                .roomId(domain.getRoomId())
                .roomName(domain.getRoomName())
                .memberId(domain.getHostMember().getMemberId())
                .creationTime(domain.getCreationTime())
                .participants(domain.getParticipants()
                        .stream()
                        .map(MemberDto::of)
                        .collect(Collectors.toList()))
                .build();
    }
}

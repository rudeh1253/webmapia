package nsl.webmapia.game.gameroom.dto;

import lombok.*;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.member.dto.MemberDto;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameRoomDto {
    private Integer roomId;
    private String roomName;
    private LocalDateTime creationTime;
    private List<ParticipationDto> participants;

    public static GameRoomDto of(GameRoom domain) {
        return GameRoomDto.builder()
                .roomId(domain.getRoomId())
                .roomName(domain.getRoomName())
                .creationTime(domain.getCreationTime())
                .participants(domain.getParticipationList().stream().map((p) ->
                    ParticipationDto.builder()
                            .participationId(p.getParticipationId())
                            .participant(MemberDto.builder()
                                    .memberId(p.getParticipant().getMemberId())
                                    .nickname(p.getParticipant().getNickname())
                                    .creationTime(p.getParticipant().getCreationTime())
                                    .build())
                            .host(p.isHost())
                            .disconnected(p.isDisconnected())
                            .gameRoomId(p.getGameRoom().getRoomId())
                            .build()).toList())
                .build();
    }
}

package nsl.webmapia.game.gameroom.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class ParticipationDto {
    private int gameRoomId;
    private String newParticipant;
    private List<String> participants;
}

package nsl.webmapia.game.domain.gameoperation.dto.response;

import lombok.*;
import nsl.webmapia.game.domain.character.domain.Faction;
import nsl.webmapia.game.domain.character.dto.CharacterAssignmentResultDto;
import nsl.webmapia.game.domain.member.dto.MemberDto;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameResultResponseDto {
    private final boolean gameEnded;
    private final List<CharacterAssignmentResultDto> characterAssignments;
    private final Faction winFaction;
    private final List<MemberDto> wolves;
    private final List<MemberDto> human;
    private final List<MemberDto> humanMouse;
}

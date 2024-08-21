package nsl.webmapia.game.gameoperation.dto.response;

import lombok.*;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;

import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class GameResultResponseDto {
    private final boolean gameEnded;
    private final Map<String, CharacterCode> charactersByMember;
    private final Faction winFaction;
    private final List<String> wolves;
    private final List<String> human;
    private final List<String> humanMouse;
}

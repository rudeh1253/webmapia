package nsl.webmapia.game.gameoperation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nsl.webmapia.game.character.domain.CharacterCode;

import java.util.Map;

/**
 * A DTO class to represent character distribution request.
 * numByCharacters indicates that for each character, how many players will be assigned to the character.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CharacterDistributionRequestDto {
    private int gameInstanceId;
    private Map<CharacterCode, Integer> numByCharacters;
}

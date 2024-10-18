package nsl.webmapia.game.gameoperation.dto.response;

import lombok.*;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.member.dto.MemberDto;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class CharacterDistributionResponseDto {
    private final Integer gameInstanceId;
    private final Map<MemberDto, CharacterCode> characterCodesByMemberIds;
}

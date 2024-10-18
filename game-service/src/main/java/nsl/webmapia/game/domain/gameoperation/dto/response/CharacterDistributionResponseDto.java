package nsl.webmapia.game.domain.gameoperation.dto.response;

import lombok.*;
import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.member.dto.MemberDto;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class CharacterDistributionResponseDto {
    private final Integer gameInstanceId;
    private final Map<MemberDto, CharacterCode> characterCodesByMemberIds;
}

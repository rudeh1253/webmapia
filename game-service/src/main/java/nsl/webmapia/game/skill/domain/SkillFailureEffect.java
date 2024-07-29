package nsl.webmapia.game.skill.domain;

import lombok.*;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.member.domain.Member;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class SkillFailureEffect {
    private final Member source;
    private final Member target;
    private final Member receiver;
    private final String message;
}

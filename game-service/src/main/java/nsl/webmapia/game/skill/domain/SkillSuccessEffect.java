package nsl.webmapia.game.skill.domain;

import lombok.*;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.member.domain.Member;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class SkillSuccessEffect {
    private final Member source;
    private final Member target;
    private final Member effectReceivers;
    private final SkillSuccessEffectType effectType;
    private final String message;

    public enum SkillSuccessEffectType {
        KILL,
        INEVITABLE_KILL,
        GUARD,
        NOTIFIED,
        ENTER_CHAT,
        NONE
    }
}

package nsl.webmapia.game.skill.domain;

import lombok.*;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class SkillSuccessEffect {
    private final String sourceId;
    private final String targetId;
    private final List<String> effectReceiverIds;
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

package nsl.webmapia.game.skill.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class SkillEffect {
    private final SkillEffectType type;
    private final String activatorId;
    private final String targetId;
    private final List<String> receiverIds;
    private final String message;

    public SkillEffect() {
        this(SkillEffectType.NONE, null, null, null, null);
    }
}

package nsl.webmapia.game.skill.domain;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class SkillFailureEffect {
    private final String sourceId;
    private final String targetId;
    private final String receiverId;
    private final String message;
}

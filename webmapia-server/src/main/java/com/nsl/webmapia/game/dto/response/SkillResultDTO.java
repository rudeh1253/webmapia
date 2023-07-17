package com.nsl.webmapia.game.dto.response;

import com.nsl.webmapia.game.domain.GameNotificationType;
import com.nsl.webmapia.game.domain.skill.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
public class SkillResultDTO {
    private final Long gameId;
    private final Long skillTargetId;
    private final Long skillActivatorId;
    private final Long receiverId;
    private final String message;
    private final GameNotificationType notificationType;
    private final CharacterEffectAfterNightType characterEffectAfterNightType;

    public static SkillResultDTO from(Long gameId, SkillEffect skillEffect, GameNotificationType notificationType) {
        Long receiverId = notificationType == GameNotificationType.SKILL_PRIVATE
                ? skillEffect.getReceiverUser().getID()
                : null;
        return SkillResultDTO.builder()
                .gameId(gameId)
                .skillTargetId(skillEffect.getSkillTargetUser().getID())
                .skillActivatorId(skillEffect.getSkillActivatorUser().getID())
                .receiverId(receiverId)
                .notificationType(notificationType)
                .characterEffectAfterNightType(skillEffect.getCharacterEffectAfterNightType())
                .build();
    }
}

package com.nsl.webmapia.skill.dto;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.skill.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.skill.domain.SkillEffect;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
@ToString
public class SkillResultResponseDTO {
    private final Long gameId;
    private final Long skillTargetId;
    private final Long skillActivatorId;
    private final Long receiverId;
    private final String message;
    private final NotificationType notificationType;
    private final CharacterEffectAfterNightType characterEffectAfterNightType;

    public static SkillResultResponseDTO from(Long gameId, SkillEffect skillEffect, NotificationType notificationType) {
        Long receiverId = notificationType == NotificationType.SKILL_PRIVATE
                ? skillEffect.getReceiverUser().getID()
                : null;
        return SkillResultResponseDTO.builder()
                .gameId(gameId)
                .skillTargetId(skillEffect.getSkillTargetUser().getID())
                .skillActivatorId(skillEffect.getSkillActivatorUser().getID())
                .receiverId(receiverId)
                .notificationType(notificationType)
                .characterEffectAfterNightType(skillEffect.getCharacterEffectAfterNightType())
                .build();
    }
}

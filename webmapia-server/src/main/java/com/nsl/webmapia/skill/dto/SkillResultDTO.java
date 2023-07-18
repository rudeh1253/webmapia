package com.nsl.webmapia.skill.dto;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.skill.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.skill.domain.SkillEffect;
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
    private final NotificationType notificationType;
    private final CharacterEffectAfterNightType characterEffectAfterNightType;

    public static SkillResultDTO from(Long gameId, SkillEffect skillEffect, NotificationType notificationType) {
        Long receiverId = notificationType == NotificationType.SKILL_PRIVATE
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

package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkillNotificationBody {
    private CharacterEffectAfterNightType characterEffectAfterNightType = null;
    private Long receiverUserId = null;
    private Long skillTargetUserId = null;
    private CharacterCode skillTargetCharacterCode = null;
    private String message = null;

    @Builder
    public SkillNotificationBody(CharacterEffectAfterNightType characterEffectAfterNightType, Long receiverUserId, Long skillTargetUserId, CharacterCode skillTargetCharacterCode, String message) {
        this.characterEffectAfterNightType = characterEffectAfterNightType;
        this.receiverUserId = receiverUserId;
        this.skillTargetUserId = skillTargetUserId;
        this.skillTargetCharacterCode = skillTargetCharacterCode;
        this.message = message;
    }
}
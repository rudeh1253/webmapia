package com.nsl.webmapia.game.domain.skill;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkillEffect {
    private CharacterEffectAfterNightType characterEffectAfterNightType = null;
    private Long receiverUserId = null;
    private Long skillTargetUserId = null;
    private Long skillActivatorUserId = null;
    private CharacterCode skillTargetCharacterCode = null;
    private String message = null;

    @Builder
    public SkillEffect(CharacterEffectAfterNightType characterEffectAfterNightType, Long receiverUserId,
                       Long skillTargetUserId, Long skillActivatorUserId,
                       CharacterCode skillTargetCharacterCode, String message) {
        this.characterEffectAfterNightType = characterEffectAfterNightType;
        this.receiverUserId = receiverUserId;
        this.skillTargetUserId = skillTargetUserId;
        this.skillActivatorUserId = skillActivatorUserId;
        this.skillTargetCharacterCode = skillTargetCharacterCode;
        this.message = message;
    }
}

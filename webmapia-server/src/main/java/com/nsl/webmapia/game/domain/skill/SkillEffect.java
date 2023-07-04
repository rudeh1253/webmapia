package com.nsl.webmapia.game.domain.skill;

import com.nsl.webmapia.game.domain.User;
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
    private User receiverUser = null;
    private User skillTargetUser = null;
    private User skillActivatorUser = null;
    private CharacterCode skillTargetCharacterCode = null;
    private String message = null;

    @Builder
    public SkillEffect(CharacterEffectAfterNightType characterEffectAfterNightType, User receiverUser,
                       User skillTargetUser, User skillActivatorUser,
                       CharacterCode skillTargetCharacterCode, String message) {
        this.characterEffectAfterNightType = characterEffectAfterNightType;
        this.receiverUser = receiverUser;
        this.skillTargetUser = skillTargetUser;
        this.skillActivatorUser = skillActivatorUser;
        this.skillTargetCharacterCode = skillTargetCharacterCode;
        this.message = message;
    }
}

package com.nsl.webmapia.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterEffectAfterNight {
    private CharacterEffectAfterNightType type;
    private User skillActivator;
    private User skillTarget;
}

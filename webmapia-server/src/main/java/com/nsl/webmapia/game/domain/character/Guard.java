package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNight;
import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Guard implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.GUARD;
    private static final Faction FACTION = Faction.HUMAN;

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect result = new SkillEffect();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> {
            for (SkillNotificationBody e : tar.getNotificationAfterNight()) {
                if (e.getCharacterEffectAfterNightType() == CharacterEffectAfterNightType.KILL) {
                    return true;
                }
            }
            return false;
        });
        result.setOnSkillSucceed((src, tar, type) -> {
            src.addNotificationAfterNight(SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.GUARD)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .skillTargetUserId(tar.getID())
                    .build());
        });
        return result;
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CHARACTER_CODE;
    }

    @Override
    public Faction getFaction() {
        return FACTION;
    }
}

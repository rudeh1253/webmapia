package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Predictor implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.PREDICTOR;
    private static final Faction FACTION = Faction.HUMAN;

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect result = new SkillEffect();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> true);
        result.setOnSkillSucceed((src, tar, type) -> {
            SkillNotificationBody notificationBody = SkillNotificationBody.builder()
                    .skillTargetUserId(tar.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .receiverUserId(src.getID())
                    .build();
            CharacterCode targetCharacterCode = tar.getCharacter().getCharacterCode();
            if (targetCharacterCode == CharacterCode.MEDIUMSHIP
            || targetCharacterCode == CharacterCode.GUARD
            || targetCharacterCode == CharacterCode.HUMAN_MOUSE) {
                notificationBody.setSkillTargetCharacterCode(targetCharacterCode);
            } else {
                notificationBody.setSkillTargetCharacterCode(CharacterCode.GOOD_MAN);
            }
            src.addMessageAfterNight(notificationBody);
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

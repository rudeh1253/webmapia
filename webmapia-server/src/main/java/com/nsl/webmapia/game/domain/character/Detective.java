package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Detective implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.DETECTIVE;
    private static final Faction FACTION = Faction.HUMAN;

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.INVESTIGATE_ALIVE_CHARACTER) {
            throw new CharacterNotSupportSkillTypeException("Detective doesn't support given skill type: SkillType code"
                    + skillType);
        }
        SkillEffect result = new SkillEffect();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> tar.getCharacter().getCharacterCode() == CharacterCode.BETRAYER
                || tar.getCharacter().getCharacterCode() == CharacterCode.FOLLOWER);
        result.setOnSkillSucceed((src, tar, type) -> {
            SkillNotificationBody skillNotificationBody = SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .skillTargetUserId(tar.getID())
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .build();
            src.addNotificationAfterNight(skillNotificationBody);
        });
        result.setOnSkillFail((src, tar, type) -> {
            SkillNotificationBody skillNotificationBody = SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .skillTargetUserId(tar.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE)
                    .build();
            src.addNotificationAfterNight(skillNotificationBody);
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

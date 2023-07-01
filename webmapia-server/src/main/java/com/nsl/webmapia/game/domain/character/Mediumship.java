package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mediumship implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.MEDIUMSHIP;
    private static final Faction FACTION = Faction.HUMAN;
    private GameManager gameManager;

    @Autowired
    public Mediumship(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.INVESTIGATE_DEAD_CHARACTER) {
            throw new CharacterNotSupportSkillTypeException("Mediumship doesn't support given skill type:"
                    + "SkillType code: " + skillType);
        }
        SkillEffect result = new SkillEffect();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> tar.isDead());
        result.setOnSkillSucceed((src, tar, type) -> {
            SkillNotificationBody notificationBody = SkillNotificationBody.builder()
                    .skillTargetUserId(tar.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .receiverUserId(src.getID())
                    .build();
            CharacterCode targetCharacterCode = tar.getCharacter().getCharacterCode();
            if (targetCharacterCode == CharacterCode.WOLF
            || targetCharacterCode == CharacterCode.GUARD
            || targetCharacterCode == CharacterCode.PREDICTOR) {
                notificationBody.setSkillTargetCharacterCode(targetCharacterCode);
            } else {
                notificationBody.setSkillTargetCharacterCode(CharacterCode.GOOD_MAN);
            }
            src.addNotificationAfterNight(notificationBody);
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

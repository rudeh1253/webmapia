package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Predictor implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.PREDICTOR;
    private static final Faction FACTION = Faction.HUMAN;
    GameManager gameManager;

    @Autowired
    public Predictor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

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
            switch (targetCharacterCode) {
                case MEDIUMSHIP:
                case GUARD:
                    notificationBody.setSkillTargetCharacterCode(targetCharacterCode);
                case HUMAN_MOUSE:
                    notificationBody.setCharacterEffectAfterNightType(CharacterEffectAfterNightType.KILL);
                    break;
                default:
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

package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.notification.PublicNotificationBody;
import com.nsl.webmapia.game.domain.notification.PublicNotificationType;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.service.PublicNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Soldier implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SOLDIER;
    private static final Faction FACTION = Faction.HUMAN;
    private PublicNotificationService publicNotificationService;
    private int life;

    @Value(value = "guard_getting_attacked")
    private String attackedMsg;

    @Autowired
    public Soldier(PublicNotificationService publicNotificationService) {
        this.publicNotificationService = publicNotificationService;
        this.life = 2;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.DEFENSE) {
            throw new CharacterNotSupportSkillTypeException("Soldier doesn't support given skill type: SkillType code " + skillType);
        }
        SkillEffect skillEffect = new SkillEffect();
        skillEffect.setSkillCondition((src, tar, type) -> --life > 0);
        skillEffect.setOnSkillSucceed((src, tar, type) -> {
            publicNotificationService.addNotification(PublicNotificationBody.builder()
                    .publicNotificationType(PublicNotificationType.GUARD)
                    .targetUserId(null)
                    .targetUserCharacterCode(null)
                    .build());
        });
        skillEffect.setSkillType(SkillType.DEFENSE);
        return skillEffect;
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

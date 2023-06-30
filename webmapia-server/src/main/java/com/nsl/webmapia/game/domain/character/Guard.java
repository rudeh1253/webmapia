package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.notification.PublicNotificationBody;
import com.nsl.webmapia.game.domain.notification.PublicNotificationType;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.service.PublicNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Guard implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.GUARD;
    private static final Faction FACTION = Faction.HUMAN;
    private PublicNotificationService publicNotificationService;

    @Autowired
    public Guard(PublicNotificationService publicNotificationService) {
        this.publicNotificationService = publicNotificationService;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect result = new SkillEffect();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> src == tar);
        result.setOnSkillSucceed((src, tar, type) -> {
            publicNotificationService.addNotification(PublicNotificationBody.builder()
                    .publicNotificationType(PublicNotificationType.GUARD)
                    .build());
        });
        result.setOnSkillFail((src, tar, type) -> {
            publicNotificationService.addNotification(PublicNotificationBody.builder()
                    .publicNotificationType(PublicNotificationType.FAIL_GUARD)
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

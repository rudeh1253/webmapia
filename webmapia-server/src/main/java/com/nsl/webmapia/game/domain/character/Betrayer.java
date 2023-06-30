package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.service.PublicNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Betrayer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.BETRAYER;
    private static final Faction FACTION = Faction.WOLF;

    private PublicNotificationService publicNotificationService;

    @Value(value = "msg_to_betrayer_when_betrayer_succeeded_find_wolf")
    private String msgToBetrayer;

    @Value(value = "msg_to_wolf_when_betrayer_succeeded_find_wolf")
    private String msgToWolf;

    @Autowired
    public Betrayer(PublicNotificationService publicNotificationService) {
        this.publicNotificationService = publicNotificationService;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.ENTER_WOLF_CHAT) {
            throw new CharacterNotSupportSkillTypeException("Betrayer doesn't support given skill type: SkillType code "
                    + skillType);
        }
        SkillEffect effect = new SkillEffect();
        effect.setSkillType(skillType);
        effect.setOnSkillSucceed((src, tar, type) -> {
            src.addMessageAfterNight(msgToBetrayer);
            tar.addMessageAfterNight(msgToWolf);
        });
        effect.setSkillCondition((src, tar, type) -> tar.getCharacter().getCharacterCode() == CharacterCode.WOLF);
        return effect;
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

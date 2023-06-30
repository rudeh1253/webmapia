package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
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
        switch (skillType) {
            case ENTER_WOLF_CHAT:
                return enterWolfChat(skillType);
            case INVESTIGATE_DEAD_CHARACTER:
                return getCharacterInfoFromDeadCharacter(skillType);
            default:
                throw new CharacterNotSupportSkillTypeException("Betrayer doesn't support given skill type: SkillType code "
                        + skillType);
        }
    }

    private SkillEffect enterWolfChat(SkillType skillType) {
        SkillEffect effect = new SkillEffect();
        effect.setSkillType(skillType);
        effect.setOnSkillSucceed((src, tar, type) -> {
            SkillNotificationBody srcBody = SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .skillTargetUserId(tar.getID())
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.ENTER_WOLF_CHAT)
                    .build();

            SkillNotificationBody tarBody = SkillNotificationBody.builder()
                    .receiverUserId(tar.getID())
                    .skillTargetUserId(null)
                    .skillTargetCharacterCode(null)
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.NOTIFY)
                    .message("Betrayer entered the wolf chat")
                    .build();

            src.addMessageAfterNight(srcBody);
            tar.addMessageAfterNight(tarBody);
        });
        effect.setOnSkillFail((src, tar, type) -> {
            src.addMessageAfterNight(SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE)
                    .build());
        });
        effect.setSkillCondition((src, tar, type) -> tar.getCharacter().getCharacterCode() == CharacterCode.WOLF);
        return effect;
    }

    private SkillEffect getCharacterInfoFromDeadCharacter(SkillType skillType) {
        SkillEffect effect = new SkillEffect();
        effect.setSkillType(skillType);
        effect.setSkillCondition((src, tar, type) -> tar.isDead());
        effect.setOnSkillSucceed((src, tar, type) -> {
            src.addMessageAfterNight(SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .skillTargetUserId(tar.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .build());
        });
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

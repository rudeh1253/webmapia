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
public class Follower implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.FOLLOWER;
    private static final Faction FACTION = Faction.WOLF;
    private boolean isAvailableInsight = true;
    private GameManager gameManager;

    @Autowired
    public Follower(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect result = new SkillEffect();
        result.setSkillType(skillType);
        switch (skillType) {
            case INVESTIGATE_ALIVE_CHARACTER:
                insight(result);
                break;
            case ENTER_WOLF_CHAT:
                enterChat(result);
                break;
            default:
                throw new CharacterNotSupportSkillTypeException("Follower doesn't support given skill type: SkillType code "
                        + skillType);
        }
        return result;
    }

    private void insight(SkillEffect result) {
        result.setSkillCondition((src, tar, type) -> true);
        result.setOnSkillSucceed((src, tar, type) -> src.addNotificationAfterNight(SkillNotificationBody.builder()
                .receiverUserId(src.getID())
                .skillTargetUserId(tar.getID())
                .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                .build()));
    }

    private void enterChat(SkillEffect result) {
        result.setSkillCondition((src, tar, type) -> tar.getCharacter().getCharacterCode() == CharacterCode.WOLF);
        result.setOnSkillSucceed((src, tar, type) -> {
            SkillNotificationBody srcBody = SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .skillTargetUserId(tar.getID())
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.ENTER_WOLF_CHAT)
                    .build();

            SkillNotificationBody tarBody = SkillNotificationBody.builder()
                    .receiverUserId(tar.getID())
                    .skillTargetUserId(tar.getID())
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.NOTIFY)
                    .message("Betrayer entered the wolf chat")
                    .build();

            src.addNotificationAfterNight(srcBody);
            tar.addNotificationAfterNight(tarBody);
        });
        result.setOnSkillFail((src, tar, type) -> {
            src.addNotificationAfterNight(SkillNotificationBody.builder()
                    .receiverUserId(src.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE)
                    .skillTargetUserId(tar.getID())
                    .build());
        });
    }

    @Override
    public CharacterCode getCharacterCode() {
        return null;
    }

    @Override
    public Faction getFaction() {
        return null;
    }

}

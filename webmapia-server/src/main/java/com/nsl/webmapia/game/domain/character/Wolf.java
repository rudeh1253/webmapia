package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Wolf implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.WOLF;
    private static final Faction FACTION = Faction.WOLF;
    private int leftExtermination;

    @Value(value = "msg_wolf_succeeded_to_kill")
    private String msgWhenKill;

    public Wolf() {
        leftExtermination = 1;
    }

    /**
     * Wolf use a skill. Wolf can use either EXTERMINATE or KILL of skill types. If using other types of skill,
     * it will throw CharacterNotSupportSkillTypeException as a RuntimeException.
     * Wolf can EXTERMINATE only once. If wolf tries using EXTERMINATE more than once, the skill type of SkillEffect
     * object returned would be set to NONE.
     * @param skillType type of skill
     * @return information of expected result of the skill. The result may vary by the other skill used by
     *         other user.
     */
    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect result = new SkillEffect();
        result.setSkillCondition((a, t, s) -> t.getCharacter().getCharacterCode() != CharacterCode.HUMAN_MOUSE);
        result.setOnSkillSucceed((a, t, s) -> {
            t.addMessageAfterNight(SkillNotificationBody.builder()
                    .receiverUserId(t.getID())
                    .skillTargetCharacterCode(t.getCharacter().getCharacterCode())
                    .skillTargetUserId(t.getID())
                    .build());
        });
        switch (skillType) {
            case EXTERMINATE:
                if (leftExtermination == 1) {
                    result.setSkillType(SkillType.EXTERMINATE);
                    leftExtermination--;
                } else {
                    result.setSkillType(SkillType.NONE);
                }
                return result;
            case KILL:
                result.setSkillType(SkillType.KILL);
                return result;
            default:
                throw new CharacterNotSupportSkillTypeException("Wolf doesn't support given skill type: SkillType code " + skillType);
        }
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

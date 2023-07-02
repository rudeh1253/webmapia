package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Wolf implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.WOLF;
    private static final Faction FACTION = Faction.WOLF;
    private int leftExtermination;
    private GameManager gameManager;

    @Autowired
    public Wolf(GameManager gameManager) {
        leftExtermination = 1;
        this.gameManager = gameManager;
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
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillCondition((a, t, s) -> t.getCharacter().getCharacterCode() != CharacterCode.HUMAN_MOUSE);
        switch (skillType) {
            case EXTERMINATE:
                if (leftExtermination == 1) {
                    result.setSkillType(SkillType.EXTERMINATE);
                    result.setOnSkillSucceed((a, t, s) -> gameManager.addSkillNotification(SkillEffect.builder()
                            .receiverUserId(t.getID())
                            .skillTargetCharacterCode(t.getCharacter().getCharacterCode())
                            .skillTargetUserId(t.getID())
                            .skillActivatorUserId(t.getID())
                            .characterEffectAfterNightType(CharacterEffectAfterNightType.EXTERMINATE)
                            .build()));
                    leftExtermination--;
                } else {
                    result.setSkillType(SkillType.NONE);
                }
                return result;
            case KILL:
                result.setSkillType(SkillType.KILL);
                result.setOnSkillSucceed((a, t, s) -> gameManager.addSkillNotification(SkillEffect.builder()
                        .receiverUserId(t.getID())
                        .skillTargetCharacterCode(t.getCharacter().getCharacterCode())
                        .skillTargetUserId(t.getID())
                        .skillActivatorUserId(t.getID())
                        .characterEffectAfterNightType(CharacterEffectAfterNightType.KILL)
                        .build()));
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

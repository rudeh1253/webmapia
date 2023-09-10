package com.nsl.webmapia.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.skill.domain.CharacterEffectAfterNightType;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.SkillEffect;
import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Wolf implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.WOLF;
    private static final Faction FACTION = Faction.WOLF;
    private int leftExtermination;
    private final SkillManager skillManager;

    @Autowired
    public Wolf(SkillManager skillManager) {
        leftExtermination = 1;
        this.skillManager = skillManager;
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
                    result.setOnSkillSucceed((a, t, s) -> skillManager.addSkillEffect(SkillEffect.builder()
                            .receiverUser(null)
                            .skillTargetCharacterCode(t.getCharacter().getCharacterCode())
                            .skillTargetUser(t)
                            .skillActivatorUser(t)
                            .characterEffectAfterNightType(CharacterEffectAfterNightType.EXTERMINATE)
                            .build()));
                    result.setOnSkillFail((src, tar, type) -> skillManager.addSkillEffect(SkillEffect.builder()
                            .receiverUser(src)
                            .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                            .skillTargetUser(tar)
                            .skillActivatorUser(src)
                            .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_EXTERMINATE)
                            .build()));
                    leftExtermination--;
                } else {
                    result.setSkillType(SkillType.NONE);
                }
                return result;
            case KILL:
                result.setSkillType(SkillType.KILL);
                result.setOnSkillSucceed((a, t, s) -> skillManager.addSkillEffect(SkillEffect.builder()
                        .receiverUser(null)
                        .skillTargetCharacterCode(t.getCharacter().getCharacterCode())
                        .skillTargetUser(t)
                        .skillActivatorUser(a)
                        .characterEffectAfterNightType(CharacterEffectAfterNightType.KILL)
                        .build()));
                result.setOnSkillFail((src, tar, type) -> skillManager.addSkillEffect(SkillEffect.builder()
                        .receiverUser(src)
                        .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                        .skillTargetUser(tar)
                        .skillActivatorUser(src)
                        .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_KILL)
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

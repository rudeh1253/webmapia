package com.nsl.webmapia.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.skill.domain.*;

public class Betrayer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.BETRAYER;
    private static final Faction FACTION = Faction.WOLF;

    /**
     * Activate one of skill of type, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER.
     * If skillType passed as a parameter is out of the two allowed SkillType, it will throw
     * CharacterNotSupportSkillTypeException as a RuntimeException.
     * @param skillType type of skill to use, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER
     *                  SkillType.ENTER_WOLF_CHAR: Check whether the target user is the wolf and if the target is the
     *                  wolf, enter the wolf chat.
     *                  SkillType.INVESTIGATE_DEAD_CHARACTER: Check the character of the dead user.
     * @return ActivatedSkillInfo
     *         Condition to succeed: the skill target has CharacterCode.WOLF.
     *         SkillEffect.receiverUserID: User id of Betrayer user
     *         SkillEffect.skillTargetUserId: User id of skill target
     *         SkillEffect.skillActivatorUserId: User id of Betrayer user
     *         SkillEffect.characterEffectAfterNightType:
     *             skillType == ENTER_WOLF_CHAT: If success, CharacterEffectAfterNightType.ENTER_WOLF_CHAT,
     *                 otherwise CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE
     *             skillType == INVESTIGATE_DEAD_CHARACTER: CharacterEffectAfterNightType.INVESTIGATE
     *         SkillEffect.skillTargetCharacterCode: CharacterCode of target character
     */
    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
        switch (skillType) {
            case ENTER_WOLF_CHAT:
                return enterWolfChat(skillManager, skillType);
            case INVESTIGATE_DEAD_CHARACTER:
                return getCharacterInfoFromDeadCharacter(skillManager, skillType);
            default:
                throw new CharacterNotSupportSkillTypeException("Betrayer doesn't support given skill type: SkillType code "
                        + skillType);
        }
    }

    private ActivatedSkillInfo enterWolfChat(SkillManager skillManager, SkillType skillType) {
        ActivatedSkillInfo effect = new ActivatedSkillInfo();
        effect.setSkillType(skillType);
        effect.setOnSkillSucceed((src, tar, type) -> {
            SkillEffect srcBody = SkillEffect.builder()
                    .receiverUser(src)
                    .skillTargetUser(tar)
                    .skillActivatorUser(src)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.ENTER_WOLF_CHAT)
                    .build();

            SkillEffect tarBody = SkillEffect.builder()
                    .receiverUser(tar)
                    .skillTargetUser(tar)
                    .skillActivatorUser(src)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.NOTIFY)
                    .message("Betrayer entered the wolf chat")
                    .build();

            skillManager.addSkillEffect(srcBody);
            skillManager.addSkillEffect(tarBody);
        });
        effect.setOnSkillFail((src, tar, type) -> {
            skillManager.addSkillEffect(SkillEffect.builder()
                    .receiverUser(src)
                    .skillActivatorUser(src)
                    .skillTargetUser(tar)
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .build());
        });
        effect.setSkillCondition((src, tar, type) -> tar.getCharacter().getFaction() == Faction.WOLF);
        return effect;
    }

    private ActivatedSkillInfo getCharacterInfoFromDeadCharacter(SkillManager skillManager, SkillType skillType) {
        ActivatedSkillInfo effect = new ActivatedSkillInfo();
        effect.setSkillType(skillType);
        effect.setSkillCondition((src, tar, type) -> tar.isDead());
        effect.setOnSkillSucceed((src, tar, type) -> {
            skillManager.addSkillEffect(SkillEffect.builder()
                    .receiverUser(src)
                    .skillActivatorUser(src)
                    .skillTargetUser(tar)
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

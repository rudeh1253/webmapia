package com.nsl.webmapia.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.skill.domain.*;

public class Follower implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.FOLLOWER;
    private static final Faction FACTION = Faction.WOLF;
    private boolean isAvailableInsight = true;

    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillType(skillType);
        switch (skillType) {
            case INVESTIGATE_ALIVE_CHARACTER:
                insight(skillManager, result);
                break;
            case ENTER_WOLF_CHAT:
                enterChat(skillManager, result);
                break;
            default:
                throw new CharacterNotSupportSkillTypeException("Follower doesn't support given skill type: SkillType code "
                        + skillType);
        }
        return result;
    }

    private void insight(SkillManager skillManager, ActivatedSkillInfo result) {
        result.setSkillCondition((src, tar, type) -> isAvailableInsight);
        result.setOnSkillSucceed((src, tar, type) -> {
            skillManager.addSkillEffect(SkillEffect.builder()
                    .receiverUser(src)
                    .skillTargetUser(tar)
                    .skillActivatorUser(src)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.INVESTIGATE)
                    .build());
            isAvailableInsight = false;
        });
    }

    private void enterChat(SkillManager skillManager, ActivatedSkillInfo result) {
        result.setSkillCondition((src, tar, type) -> tar.getCharacter().getFaction() == Faction.WOLF);
        result.setOnSkillSucceed((src, tar, type) -> {
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
        result.setOnSkillFail((src, tar, type) -> {
            skillManager.addSkillEffect(SkillEffect.builder()
                    .receiverUser(src)
                    .skillActivatorUser(src)
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE)
                    .skillTargetUser(tar)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .build());
        });
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

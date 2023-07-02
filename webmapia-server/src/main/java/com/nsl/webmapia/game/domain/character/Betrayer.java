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
public class Betrayer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.BETRAYER;
    private static final Faction FACTION = Faction.WOLF;
    private GameManager gameManager;

    @Autowired
    public Betrayer(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
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

    private ActivatedSkillInfo enterWolfChat(SkillType skillType) {
        ActivatedSkillInfo effect = new ActivatedSkillInfo();
        effect.setSkillType(skillType);
        effect.setOnSkillSucceed((src, tar, type) -> {
            SkillEffect srcBody = SkillEffect.builder()
                    .receiverUserId(src.getID())
                    .skillTargetUserId(tar.getID())
                    .skillActivatorUserId(src.getID())
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.ENTER_WOLF_CHAT)
                    .build();

            SkillEffect tarBody = SkillEffect.builder()
                    .receiverUserId(tar.getID())
                    .skillTargetUserId(tar.getID())
                    .skillActivatorUserId(src.getID())
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.NOTIFY)
                    .message("Betrayer entered the wolf chat")
                    .build();

            gameManager.addSkillEffect(srcBody);
            gameManager.addSkillEffect(tarBody);
        });
        effect.setOnSkillFail((src, tar, type) -> {
            gameManager.addSkillEffect(SkillEffect.builder()
                    .receiverUserId(src.getID())
                    .skillActivatorUserId(src.getID())
                    .skillTargetUserId(tar.getID())
                    .characterEffectAfterNightType(CharacterEffectAfterNightType.FAIL_TO_INVESTIGATE)
                    .skillTargetCharacterCode(tar.getCharacter().getCharacterCode())
                    .build());
        });
        effect.setSkillCondition((src, tar, type) -> tar.getCharacter().getCharacterCode() == CharacterCode.WOLF);
        return effect;
    }

    private ActivatedSkillInfo getCharacterInfoFromDeadCharacter(SkillType skillType) {
        ActivatedSkillInfo effect = new ActivatedSkillInfo();
        effect.setSkillType(skillType);
        effect.setSkillCondition((src, tar, type) -> tar.isDead());
        effect.setOnSkillSucceed((src, tar, type) -> {
            gameManager.addSkillEffect(SkillEffect.builder()
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

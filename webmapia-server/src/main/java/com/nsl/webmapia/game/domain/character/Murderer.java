package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Murderer implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.MURDERER;
    private static final Faction FACTION = Faction.HUMAN;
    private boolean canKill = true;
    private GameManager gameManager;

    @Autowired
    public Murderer(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.EXTERMINATE) {
            throw new CharacterNotSupportSkillTypeException("Murderer doesn't support given skill type: SkillType code"
                    + skillType);
        }
        SkillEffect result = new SkillEffect();
        result.setSkillType(skillType);
        result.setSkillCondition((src, tar, type) -> canKill);
        result.setOnSkillSucceed((src, tar, type) -> canKill = false);
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

package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Citizen implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.CITIZEN;
    private static final Faction FACTION = Faction.HUMAN;
    private GameManager gameManager;

    @Autowired
    public Citizen(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect result = new SkillEffect();
        result.setSkillType(SkillType.NONE);
        result.setSkillCondition((src, tar, type) -> false);
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

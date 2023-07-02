package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Nobility implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.NOBILITY;
    private static final Faction FACTION = Faction.HUMAN;
    private GameManager gameManager;

    @Autowired
    public Nobility(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
        ActivatedSkillInfo result = new ActivatedSkillInfo();
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

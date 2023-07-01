package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HumanMouse implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.HUMAN_MOUSE;
    private static final Faction FACTION = Faction.HUMAN_MOUSE;
    private GameManager gameManager;

    @Autowired
    public HumanMouse(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        SkillEffect skillEffect = new SkillEffect();
        skillEffect.setSkillType(SkillType.NONE);
        skillEffect.setSkillCondition((src, tar, type) -> false);
        return skillEffect;
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

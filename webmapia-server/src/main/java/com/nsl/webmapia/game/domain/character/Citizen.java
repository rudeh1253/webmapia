package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.SkillManager;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Citizen implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.CITIZEN;
    private static final Faction FACTION = Faction.HUMAN;
    private SkillManager skillManager;

    @Autowired
    public Citizen(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    /**
     * Citizen doesn't have any skill.
     * @param skillType type of skill to use, any type of skill is allowed.
     * @return skill activated by citizen doesn't have any effect.
     *         ActivatedSkillInfo.skillType: SkillType.NONE
     *         ActivatedSkillInfo.skillCondition: always false
     *         ActivatedSkillInfo.onSkillFail: No effect
     */
    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
        ActivatedSkillInfo result = new ActivatedSkillInfo();
        result.setSkillType(SkillType.NONE);
        result.setSkillCondition((src, tar, type) -> false);
        result.setOnSkillFail((src, tar, type) -> {});
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

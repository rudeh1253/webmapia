package com.nsl.webmapia.character;

import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.SkillType;

public class Citizen implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.CITIZEN;
    private static final Faction FACTION = Faction.HUMAN;

    /**
     * Citizen doesn't have any skill.
     * @param skillType type of skill to use, any type of skill is allowed.
     * @return skill activated by citizen doesn't have any effect.
     *         ActivatedSkillInfo.skillType: SkillType.NONE
     *         ActivatedSkillInfo.skillCondition: always false
     *         ActivatedSkillInfo.onSkillFail: No effect
     */
    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
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

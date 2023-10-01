package com.nsl.webmapia.character;

import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.SkillType;

public class HumanMouse implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.HUMAN_MOUSE;
    private static final Faction FACTION = Faction.HUMAN_MOUSE;

    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
        ActivatedSkillInfo activatedSkillInfo = new ActivatedSkillInfo();
        activatedSkillInfo.setSkillType(SkillType.NONE);
        activatedSkillInfo.setSkillCondition((src, tar, type) -> false);
        return activatedSkillInfo;
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

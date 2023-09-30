package com.nsl.webmapia.character;

import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillType;

public class SecretSociety implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SECRET_SOCIETY;
    private static final Faction FACTION = Faction.HUMAN;

    @Override
    public ActivatedSkillInfo activateSkill(SkillManager skillManager, SkillType skillType) {
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

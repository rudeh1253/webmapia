package com.nsl.webmapia.character;

import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Templar implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.TEMPLAR;
    private static final Faction FACTION = Faction.HUMAN;
    private SkillManager skillManager;

    @Autowired
    public Templar(SkillManager skillManager) {
        this.skillManager = skillManager;
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

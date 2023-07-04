package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HumanMouse implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.HUMAN_MOUSE;
    private static final Faction FACTION = Faction.HUMAN_MOUSE;
    private SkillManager skillManager;

    @Autowired
    public HumanMouse(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public ActivatedSkillInfo activateSkill(SkillType skillType) {
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

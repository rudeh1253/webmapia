package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecretSociety implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.SECRET_SOCIETY;
    private static final Faction FACTION = Faction.HUMAN;
    private SkillManager skillManager;

    @Autowired
    public SecretSociety(SkillManager skillManager) {
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

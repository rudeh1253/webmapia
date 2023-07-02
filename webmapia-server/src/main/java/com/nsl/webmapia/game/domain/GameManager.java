package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.skill.SkillEffect;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameManager {
    private final List<SkillEffect> skillEffects;

    public GameManager() {
        this.skillEffects = new ArrayList<>();
    }

    public void addSkillEffect(SkillEffect skillNotification) {
        skillEffects.add(skillNotification);
    }

    public List<SkillEffect> getSkillEffects() {
        return skillEffects;
    }

    public void clearSkillEffects() {
        skillEffects.clear();
    }
}

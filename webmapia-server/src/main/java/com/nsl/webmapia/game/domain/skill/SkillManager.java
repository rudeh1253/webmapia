package com.nsl.webmapia.game.domain.skill;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SkillManager {
    private final List<SkillEffect> skillEffects;

    public SkillManager() {
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

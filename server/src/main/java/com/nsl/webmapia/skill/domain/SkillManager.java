package com.nsl.webmapia.skill.domain;

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
        this.skillEffects.add(skillNotification);
    }

    public List<SkillEffect> getSkillEffects() {
        List<SkillEffect> toReturn = new ArrayList<>();
        toReturn.addAll(this.skillEffects);
        return toReturn;
    }

    public void clearSkillEffects() {
        skillEffects.clear();
    }
}

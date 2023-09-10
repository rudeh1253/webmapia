package com.nsl.webmapia.skill.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SkillManager {
    private final List<SkillEffect> skillEffects;

    public SkillManager() {
        System.out.println("SkillManager Instantiated");
        this.skillEffects = new ArrayList<>();
    }

    public void addSkillEffect(SkillEffect skillNotification) {
        System.out.println("skillNotification = " + skillNotification);
        this.skillEffects.add(skillNotification);
        System.out.println("1. skillEffects = " + skillEffects);
    }

    public List<SkillEffect> getSkillEffects() {
        System.out.println("2. skillEffects = " + skillEffects);
        List<SkillEffect> toReturn = new ArrayList<>();
        toReturn.addAll(this.skillEffects);
        return toReturn;
    }

    public void clearSkillEffects() {
        skillEffects.clear();
    }
}

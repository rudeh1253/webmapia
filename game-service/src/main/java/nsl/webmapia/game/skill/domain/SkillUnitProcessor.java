package nsl.webmapia.game.skill.domain;

import nsl.webmapia.game.character.entity.CharacterAssignment;

import java.util.Set;

@FunctionalInterface
public interface SkillUnitProcessor {

    /**
     * For each skill, process it based on the condition.
     *
     * @param activator who used the skill
     * @param target of the skill
     * @param activatedSkillsToTarget activated to target skills, of that night, of that game
     * @return SkillEffect instance contains result of skill
     */
    SkillEffect process(CharacterAssignment activator,
                        CharacterAssignment target,
                        Set<SkillType> activatedSkillsToTarget);
}

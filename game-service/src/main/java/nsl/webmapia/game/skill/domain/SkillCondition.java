package nsl.webmapia.game.skill.domain;

import nsl.webmapia.game.character.domain.Character;

import java.util.Set;

@FunctionalInterface
public interface SkillCondition {

    /**
     * Check condition of skill and based on the condition, determine whether
     * the skill is successful or not.
     * @param activator who used the skill
     * @param target of the skill
     * @param activatedSkillsToTarget activated to target skills, of that night, of that game
     * @return true if successful, otherwise false.
     */
    boolean isSuccess(Character activator,
                      Character target,
                      Set<SkillType> activatedSkillsToTarget);
}

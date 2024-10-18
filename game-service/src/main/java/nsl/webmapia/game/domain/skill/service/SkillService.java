package nsl.webmapia.game.domain.skill.service;

import nsl.webmapia.game.domain.skill.domain.SkillEffect;
import nsl.webmapia.game.domain.skill.domain.SkillType;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A Service specification interface for managing skills.
 *
 * @author PGD
 */
public interface SkillService {

    Map<SkillType, List<Integer>> getAvailableSkills(int gameInstanceId, Integer characterAssignmentId)
            throws IllegalStateException;

    /**
     * Activate skill of given round.
     *
     * @param gameInstanceId of GameInstance on running currently
     * @param activatorCharacterAssignmentId who activated the skill
     * @param targetCharacterAssignmentId which is the target of the skill
     * @param skillType which the member activated
     *
     * TODO: another exception is needed here.
     * @throws IllegalStateException when the gamePhase of GameInstance is not NIGHT.
     * @throws NoSuchElementException when for given gameInstanceId the GameInstance doesn't exist.
     */
    void activateSkill(int gameInstanceId, Integer activatorCharacterAssignmentId, Integer targetCharacterAssignmentId, SkillType skillType)
            throws IllegalStateException, NoSuchElementException;

    List<SkillEffect> processSkills(int gameInstanceId);
}

package nsl.webmapia.game.skill.service;

import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.skill.domain.SkillType;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A Service specification interface for managing skills.
 *
 * @author PGD
 */
public interface SkillService {

    BaseSystemMessageResponseDto<List<SkillType>> getAvailableSkills(int gameRoomId, String memberId);

    /**
     * Activate skill of given round.
     *
     * @param gameRoomId of GameInstance on running currently
     * @param activatorId who activated the skill
     * @param targetId which is the target of the skill
     * @param skillType which the member activated
     *
     * TODO: another exception is needed here.
     * @throws IllegalStateException when the gamePhase of GameInstance is not NIGHT.
     * @throws NoSuchElementException when for given gameRoomId the GameInstance doesn't exist.
     */
    void activateSkill(int gameRoomId, String activatorId, String targetId, SkillType skillType)
            throws IllegalStateException, NoSuchElementException;


}

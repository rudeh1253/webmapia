package com.nsl.webmapia.skill.service;

import com.nsl.webmapia.skill.dto.SkillResultResponseDTO;
import com.nsl.webmapia.skill.domain.SkillType;

import java.util.List;

public interface SkillService {

    /**
     * Apply skill. The process of the skill follows the logic of the skill of the character the user
     * who was allocated the character activated. The effect of the skill is going to be processed by calling
     * processSkills(Long) method.
     * @param gameId id of game.
     * @param activatorId of user which activated skill.
     * @param targetId of user which is the target of the skill.
     * @param skillType of skill activated.
     */
    void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType);

    /**
     * After the night, process activated skills.
     * @param gameId id of game.
     * @return a list of DTO representing skill result. If the notification is for public broadcast,
     *         the receiver id is set to null.
     */
    List<SkillResultResponseDTO> processSkills(Long gameId);
}

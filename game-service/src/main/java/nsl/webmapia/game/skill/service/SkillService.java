package nsl.webmapia.game.skill.service;

import nsl.webmapia.game.skill.domain.SkillType;

import java.util.List;

/**
 * A Service specification interface for managing skills.
 *
 * @author PGD
 */
public interface SkillService {

    List<SkillType> getAvailableSkills(int gameInstanceId, String memberId);
}

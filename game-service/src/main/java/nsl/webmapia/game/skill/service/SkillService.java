package nsl.webmapia.game.skill.service;

import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.skill.domain.SkillType;

import java.util.List;

/**
 * A Service specification interface for managing skills.
 *
 * @author PGD
 */
public interface SkillService {

    BaseSystemMessageResponseDto<List<SkillType>> getAvailableSkills(int gameRoomId, String memberId);
}

package nsl.webmapia.game.character.repository;

import nsl.webmapia.game.character.entity.CharacterAssignment;

import java.util.List;
import java.util.Optional;

/**
 * Repository that stores for a GameInstance, what character a member has been
 * assigned.
 */
public interface CharacterAssignmentRepository {

    void save(CharacterAssignment characterAssignment);

    Optional<CharacterAssignment> findById(Integer id);

    List<CharacterAssignment> findByGameInstanceId(int gameInstanceId);

    List<CharacterAssignment> findDeadCharacterAssignmentsByGameInstanceId(int gameInstanceId);

    List<CharacterAssignment> findAliveCharacterAssignmentsByGameInstanceId(int gameInstanceId);

    /**
     * Find CharacterAssignment instance by gameRoomId and memberId from GameInstance of gameInstanceId which is
     * alive (i.e. endTime is null).
     *
     * @param gameInstanceId of GameInstance
     * @param memberId       of CharacterAssignment
     * @return CharacterAssignment instance given conditions. Optional instance can be empty.
     */

    Optional<CharacterAssignment> findByGameInstanceIdAndMemberId(int gameInstanceId, String memberId);

    void updateLifeById(Integer characterAssignmentId, int life);
}

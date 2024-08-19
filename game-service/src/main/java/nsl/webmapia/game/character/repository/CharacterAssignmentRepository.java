package nsl.webmapia.game.character.repository;

import nsl.webmapia.game.character.entity.CharacterAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository that stores for a GameInstance, what character a member has been
 * assigned.
 */
public interface CharacterAssignmentRepository {

    void save(CharacterAssignment characterAssignment);

    List<CharacterAssignment> findByGameInstanceId(int gameInstanceId);

    List<CharacterAssignment> findByGameRoomId(int gameRoomId);

    List<CharacterAssignment> findDeadCharacterAssignmentsByGameRoomId(int gameRoomId);

    Optional<CharacterAssignment> findByGameInstanceIdAndMemberId(int gameInstanceId, String memberId);

    /**
     * Find CharacterAssignment instance by gameRoomId and memberId from GameInstance of gameRoomId which is
     * alive (i.e. endTime is null).
     *
     * @param gameRoomId of GameInstance
     * @param memberId   of CharacterAssignment
     * @return CharacterAssignment instance given conditions. Optional instance can be empty.
     */
    Optional<CharacterAssignment> findByGameRoomIdAndMemberId(int gameRoomId, String memberId);

    void updateLifeByGameRoomIdAndMemberId(int gameRoomId, String memberId, int life);
}

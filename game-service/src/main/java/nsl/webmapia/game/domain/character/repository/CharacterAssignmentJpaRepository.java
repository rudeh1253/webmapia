package nsl.webmapia.game.domain.character.repository;

import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CharacterAssignmentJpaRepository extends JpaRepository<CharacterAssignment, Integer> {

    @Query("""
            SELECT ca FROM CharacterAssignment ca
            WHERE ca.gameInstance.gameInstanceId = :gameInstanceId
            """)
    List<CharacterAssignment> findByGameInstanceId(int gameInstanceId);

    /**
     * Find CharacterAssignment instance by gameRoomId and memberId from GameInstance of gameRoomId which is
     * alive (i.e. endTime is null).
     *
     * @param gameInstanceId of GameInstance
     * @param memberId   of CharacterAssignment
     * @return CharacterAssignment instance given conditions. Optional instance can be empty.
     */
    @Query("""
            SELECT ca FROM CharacterAssignment ca
            WHERE ca.gameInstance.gameInstanceId = :gameInstanceId
                AND ca.member.memberId = :memberId
            """)
    Optional<CharacterAssignment> findByGameInstanceIdAndMemberId(int gameInstanceId, String memberId);

    @Query("""
            SELECT ca FROM CharacterAssignment ca
            WHERE ca.gameInstance.gameInstanceId = :gameInstanceId
                AND ca.life = 0
            """)
    List<CharacterAssignment> findDeadCharacterAssignmentsByGameInstanceId(int gameInstanceId);

    @Query("""
            SELECT ca FROM CharacterAssignment ca
            WHERE ca.gameInstance.gameInstanceId = :gameInstanceId
                AND ca.life != 0
            """)
    List<CharacterAssignment> findAliveCharacterAssignmentsByGameInstanceId(int gameInstanceId);
}

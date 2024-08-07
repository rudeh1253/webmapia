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
public interface CharacterAssignmentRepository extends JpaRepository<CharacterAssignment, Integer> {

    @Query("""
            SELECT ca FROM CharacterAssignment ca
            WHERE ca.gameInstance.gameInstanceId = :gameInstanceId
            """)
    List<CharacterAssignment> findByGameInstanceId(int gameInstanceId);

    @Query("""
            SELECT ca FROM CharacterAssignment ca
            WHERE ca.gameInstance.gameInstanceId = :gameInstanceId
                AND ca.memberId = :memberId
            """)
    Optional<CharacterAssignment> findByGameInstanceIdAndMemberId(int gameInstanceId, String memberId);
}

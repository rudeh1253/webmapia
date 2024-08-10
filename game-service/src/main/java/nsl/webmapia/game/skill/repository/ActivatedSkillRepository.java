package nsl.webmapia.game.skill.repository;

import nsl.webmapia.game.skill.entity.ActivatedSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivatedSkillRepository extends JpaRepository<ActivatedSkill, Integer> {

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.gameInstance.gameInstanceId = :gameInstanceId
                AND a.round = :round
            """)
    List<ActivatedSkill> findByGameInstanceIdAndRound(int gameInstanceId, int round);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.gameInstance.gameInstanceId = :gameInstanceId
                AND a.characterAssignment.assignmentId = :characterAssignmentId
            """)
    List<ActivatedSkill> findByGameInstanceIdAndCharacterAssignmentId(int gameInstanceId, int characterAssignmentId);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.gameInstance.gameRoom.roomId = :gameRoomId
                AND a.gameInstance.endTime IS NULL
                AND a.characterAssignment.memberId = :memberId
            """)
    List<ActivatedSkill> findByGameRoomIdAndMemberId(int gameRoomId, String memberId);
}

package nsl.webmapia.game.skill.repository;

import nsl.webmapia.game.skill.entity.ActivatedSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivatedSkillRepository extends JpaRepository<ActivatedSkill, Integer> {

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.activator.gameInstance.gameInstanceId = :gameInstanceId
                AND a.round = :round
            """)
    List<ActivatedSkill> findByGameInstanceIdAndRound(int gameInstanceId, int round);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.activator.gameInstance.gameRoom.roomId = :gameRoomId
                AND a.activator.gameInstance.endTime IS NULL
                AND a.round = :round
            """)
    List<ActivatedSkill> findByGameRoomIdAndRound(int gameRoomId, int round);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.activator.gameInstance.gameInstanceId = :gameInstanceId
                AND a.activator.memberId = :activatorId
            """)
    List<ActivatedSkill> findByGameInstanceIdAndActivatorId(int gameInstanceId, String activatorId);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.activator.gameInstance.gameRoom.roomId = :gameRoomId
                AND a.activator.gameInstance.endTime IS NULL
                AND a.activator.memberId = :activatorId
            """)
    List<ActivatedSkill> findByGameRoomIdAndActivatorId(int gameRoomId, String activatorId);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.activator.gameInstance.gameRoom.roomId = :gameRoomId
                AND a.activator.gameInstance.endTime IS NULL
                AND a.target.memberId = :targetId
            """)
    List<ActivatedSkill> findByGameRoomIdAndTargetId(int gameRoomId, String targetId);
}

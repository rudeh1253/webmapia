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
                AND a.activatorId = :activatorId
            """)
    List<ActivatedSkill> findByGameInstanceIdAndActivatorId(int gameInstanceId, int activatorId);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.gameInstance.gameRoom.roomId = :gameRoomId
                AND a.gameInstance.endTime IS NULL
                AND a.activatorId = :activatorId
            """)
    List<ActivatedSkill> findByGameRoomIdAndActivatorId(int gameRoomId, String activatorId);

    @Query("""
            SELECT a FROM ActivatedSkill a
            WHERE a.gameInstance.gameRoom.roomId = :gameRoomId
                AND a.gameInstance.endTime IS NULL
                AND a.targetId = :targetId
            """)
    List<ActivatedSkill> findByGameRoomIdAndTargetId(int gameRoomId, String targetId);
}

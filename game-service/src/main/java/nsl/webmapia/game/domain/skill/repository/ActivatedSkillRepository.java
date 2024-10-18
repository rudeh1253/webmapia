package nsl.webmapia.game.domain.skill.repository;

import nsl.webmapia.game.domain.skill.entity.ActivatedSkill;
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
            WHERE a.activator.gameInstance.gameInstanceId = :gameInstanceId
                AND a.activator.assignmentId = :activatorId
            """)
    List<ActivatedSkill> findByGameInstanceIdAndActivatorId(int gameInstanceId, Integer activatorId);
}

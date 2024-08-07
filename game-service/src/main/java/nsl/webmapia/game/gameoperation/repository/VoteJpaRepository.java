package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface VoteJpaRepository extends JpaRepository<Vote, Integer> {

    @Query("""
            SELECT v FROM Vote v
            WHERE v.gameInstance.gameInstanceId = :gameInstanceId
                AND v.voteId.round = :round
            """)
    Set<Vote> findByGameInstanceIdAndRound(int gameInstanceId, int round);
}

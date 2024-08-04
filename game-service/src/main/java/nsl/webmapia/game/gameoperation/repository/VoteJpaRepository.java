package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteJpaRepository extends JpaRepository<Vote, Integer> {
}

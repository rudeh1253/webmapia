package nsl.webmapia.game.gameoperation.repository;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.gameoperation.domain.Vote;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaVoteRepository implements VoteRepository {
    private final VoteJpaRepository voteJpaRepository;

    @Override
    public void save(Vote vote) {
        this.voteJpaRepository.save(vote);
    }

    @Override
    public Set<Vote> findByGameInstanceAndRound(GameInstance gameInstance, int round) {
        return this.voteJpaRepository.findByGameInstanceIdAndRound(gameInstance.getGameInstanceId(), round);
    }
}

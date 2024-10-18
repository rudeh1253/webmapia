package nsl.webmapia.game.domain.vote.repository;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.vote.entity.Vote;
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
    public Set<Vote> findByGameInstanceIdAndRound(int gameInstanceId, int round) {
        return this.voteJpaRepository.findByGameInstanceIdAndRound(gameInstanceId, round);
    }
}

package nsl.webmapia.game.domain.vote.repository;

import nsl.webmapia.game.domain.vote.entity.Vote;

import java.util.Set;

public interface VoteRepository {

    void save(Vote vote);

    Set<Vote> findByGameInstanceIdAndRound(int gameInstanceId, int round);
}

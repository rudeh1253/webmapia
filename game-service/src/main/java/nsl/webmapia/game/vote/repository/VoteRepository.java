package nsl.webmapia.game.vote.repository;

import nsl.webmapia.game.vote.entity.Vote;

import java.util.Set;

public interface VoteRepository {

    void save(Vote vote);

    Set<Vote> findByGameInstanceIdAndRound(int gameInstanceId, int round);
}

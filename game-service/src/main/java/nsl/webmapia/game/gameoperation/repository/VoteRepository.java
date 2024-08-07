package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.entity.Vote;

import java.util.Set;

public interface VoteRepository {

    void save(Vote vote);

    Set<Vote> findByGameInstanceIdAndRound(int gameInstanceId, int round);
}

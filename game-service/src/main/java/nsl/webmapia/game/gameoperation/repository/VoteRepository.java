package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.gameoperation.domain.Vote;

import java.util.Set;

public interface VoteRepository {

    void save(Vote vote);

    Set<Vote> findByGameInstanceAndRound(GameInstance gameInstance, int round);
}

package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.entity.Vote;

import java.util.Set;

public interface VoteRepository {

    void save(Vote vote);

    Set<Vote> findByGameInstanceAndRound(GameInstance gameInstance, int round);
}

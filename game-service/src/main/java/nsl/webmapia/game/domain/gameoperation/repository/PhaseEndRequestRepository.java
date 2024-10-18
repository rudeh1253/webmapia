package nsl.webmapia.game.domain.gameoperation.repository;

import java.util.Optional;
import java.util.Set;

/**
 * A repository to save request of ending phase from clients.
 * @author PGD
 */
public interface PhaseEndRequestRepository {

    void savePhaseEndRequest(int gameInstanceId, String memberId);

    void savePhaseEndObjective(int gameInstanceId, int objectiveNum);

    Set<String> findPhaseEndRequestsByGameInstanceId(int gameInstanceId);

    Optional<Integer> findPhaseEndObjectiveByGameInstanceId(int gameInstanceId);

    boolean deleteByGameInstanceId(int gameInstanceId);
}

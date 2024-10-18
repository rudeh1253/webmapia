package nsl.webmapia.game.domain.gameoperation.repository;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This may be replaced with Redis
 * @author PGD
 */
@RequiredArgsConstructor
public class InMemoryPhaseEndRequestRepository implements PhaseEndRequestRepository {
    private static final Map<Integer, Set<String>> phaseEndRequestersByGameInstanceId = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> phaseEndObjectiveByGameInstanceId = new ConcurrentHashMap<>();

    @Override
    public void savePhaseEndRequest(int gameInstanceId, String memberId) {
        if (!phaseEndRequestersByGameInstanceId.containsKey(gameInstanceId)) {
            synchronized (this) {
                if (!phaseEndRequestersByGameInstanceId.containsKey(gameInstanceId)) {
                    Set<String> requesterSet = ConcurrentHashMap.newKeySet();
                    phaseEndRequestersByGameInstanceId.put(gameInstanceId, requesterSet);
                }
            }
        }
        Set<String> requesters = phaseEndRequestersByGameInstanceId.get(gameInstanceId);
        requesters.add(memberId);
    }

    @Override
    public void savePhaseEndObjective(int gameInstanceId, int objectiveNum) {
        phaseEndObjectiveByGameInstanceId.put(gameInstanceId, objectiveNum);
    }

    @Override
    public Set<String> findPhaseEndRequestsByGameInstanceId(int gameInstanceId) {
        return phaseEndRequestersByGameInstanceId.containsKey(gameInstanceId)
                ? phaseEndRequestersByGameInstanceId.get(gameInstanceId)
                : Set.of();
    }

    @Override
    public Optional<Integer> findPhaseEndObjectiveByGameInstanceId(int gameInstanceId) {
        return Optional.ofNullable(phaseEndObjectiveByGameInstanceId.get(gameInstanceId));
    }

    @Override
    public boolean deleteByGameInstanceId(int gameInstanceId) {
        Set<String> requestRemoved = phaseEndRequestersByGameInstanceId.remove(gameInstanceId);
        Integer objectiveRemoved = phaseEndObjectiveByGameInstanceId.remove(gameInstanceId);
        return requestRemoved != null && objectiveRemoved != null;
    }

    public void clear() {
        phaseEndObjectiveByGameInstanceId.clear();
        phaseEndRequestersByGameInstanceId.clear();
    }
}

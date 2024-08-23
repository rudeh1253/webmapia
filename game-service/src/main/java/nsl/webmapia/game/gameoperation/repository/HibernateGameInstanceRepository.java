package nsl.webmapia.game.gameoperation.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HibernateGameInstanceRepository implements GameInstanceRepository {
    private final EntityManager em;

    @Override
    public void save(GameInstance gameInstance) {
        if (gameInstance.getRound() < 0) {
            throw new DataIntegrityViolationException("gameInstance.round should be over 0, but " + gameInstance.getRound());
        }
        this.em.persist(gameInstance);
    }

    @Override
    public Optional<GameInstance> findById(int id) {
        GameInstance findGameInstance = this.em.find(GameInstance.class, id);
        return Optional.ofNullable(findGameInstance);
    }

    @Override
    public Optional<GameInstance> findAliveGameInstanceByGameRoomId(int gameRoomId) throws IllegalStateException {
        try {
            GameInstance gameInstance = this.em.createQuery("""
                        SELECT gi FROM GameInstance gi
                        WHERE gi.gameRoom.roomId = :gameRoomId
                            AND gi.endTime IS NULL
                        """, GameInstance.class)
                    .setParameter("gameRoomId", gameRoomId)
                    .getSingleResult();
            return Optional.ofNullable(gameInstance);
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException(
                    "There are more than two gameInstance of gameRoomId=" + gameRoomId + ", each of which endTime is null",
                    e
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean updateByGameRoomId(GameInstanceUpdateDto dto) {
        Optional<GameInstance> gameInstanceOp = findById(dto.getGameInstanceId());
        if (gameInstanceOp.isEmpty()) {
            return false;
        }
        GameInstance gameInstance = gameInstanceOp.get();

        if (dto.getRound() != null) {
            gameInstance.setRound(dto.getRound());
        }
        if (dto.getStartTime() != null) {
            gameInstance.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            gameInstance.setEndTime(dto.getEndTime());
        }
        if (dto.getGamePhase() != null) {
            gameInstance.setGamePhase(dto.getGamePhase());
        }
        if (dto.getPhaseEndTime() != null) {
            gameInstance.setPhaseEndTime(dto.getPhaseEndTime());
        }
        return true;
    }
}

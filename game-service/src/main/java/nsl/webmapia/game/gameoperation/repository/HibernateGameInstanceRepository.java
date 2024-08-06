package nsl.webmapia.game.gameoperation.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HibernateGameInstanceRepository implements GameInstanceRepository {
    private final EntityManager em;


    @Override
    public void save(GameInstance gameInstance) {
        this.em.persist(gameInstance);
    }

    @Override
    public Optional<GameInstance> findById(int id) {
        GameInstance findGameInstance = this.em.find(GameInstance.class, id);
        return Optional.ofNullable(findGameInstance);
    }
}

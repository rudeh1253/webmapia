package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.entity.GameInstance;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class InMemoryGameInstanceRepository implements GameInstanceRepository {
    private static final Map<Integer, GameInstance> indexedById = new ConcurrentHashMap<>();

    @Override
    public void save(GameInstance gameInstance) throws IllegalArgumentException {
        if (gameInstance.getGameRoom() == null || gameInstance.getGameRoom().getRoomId() == null) {
            throw new IllegalArgumentException();
        }
        indexedById.put(gameInstance.getGameRoom().getRoomId(), gameInstance);
    }

    @Override
    public Optional<GameInstance> findById(int id) {
        return Optional.ofNullable(indexedById.get(id));
    }

    @Override
    public Optional<GameInstance> findAliveGameInstanceByGameRoomId(int gameRoomId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateByGameRoomId(GameInstanceUpdateDto dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsAliveGameInstanceByGameRoomId(int gameRoomId) throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        indexedById.clear();
    }
}

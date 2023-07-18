package com.nsl.webmapia.gameoperation.repository;

import com.nsl.webmapia.gameoperation.domain.GameManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryGameRepository implements GameRepository {
    private final Map<Long, GameManager> store;

    public MemoryGameRepository() {
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public void save(GameManager game) {
        store.put(game.getGameId(), game);
    }

    @Override
    public Optional<GameManager> findById(Long gameId) {
        return Optional.ofNullable(store.get(gameId));
    }

    @Override
    public List<GameManager> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean containsKey(Long gameId) {
        return store.containsKey(gameId);
    }

    @Override
    public int countAll() {
        return store.size();
    }

    @Override
    public Optional<GameManager> deleteGameById(Long gameId) {
        return Optional.ofNullable(store.remove(gameId));
    }

    public void clear() {
        store.clear();
    }
}

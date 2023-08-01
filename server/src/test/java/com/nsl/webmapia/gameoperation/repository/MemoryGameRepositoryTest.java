package com.nsl.webmapia.gameoperation.repository;

import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.gameoperation.domain.GameManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryGameRepositoryTest {
    MemoryGameRepository repository = new MemoryGameRepository();

    @BeforeEach
    public void intiailize() {
        repository.clear();
    }

    @Test
    void findById() {
        GameManager gameManager = new GameManagerImpl(1L, null, null, null);
        repository.save(gameManager);

        repository.findById(1L).ifPresent(e -> assertEquals(1L, e.getGameId()));
    }

    @Test
    void findById_fail() {
        GameManager gameManager = new GameManagerImpl(1L, null, null, null);
        repository.save(gameManager);

        assertTrue(repository.findById(2L).isEmpty());
    }

    @Test
    void findAll() {
        GameManager game1 = new GameManagerImpl(1L, null, null, null);
        GameManager game2 = new GameManagerImpl(2L, null, null, null);
        GameManager game3 = new GameManagerImpl(3L, null, null, null);
        GameManager game4 = new GameManagerImpl(4L, null, null, null);
        repository.save(game1);
        repository.save(game2);
        repository.save(game3);
        repository.save(game4);

        List<GameManager> games = repository.findAll();
        assertEquals(4, games.size());
        for (int i = 1; i <= 4; i++) {
            assertEquals(Long.valueOf(i), games.get(i - 1).getGameId());
        }
    }

    @Test
    void containsKey() {
        GameManager game = new GameManagerImpl(1L, null, null, null);
        repository.save(game);
        assertTrue(repository.containsKey(1L));
        assertFalse(repository.containsKey(2L));
    }

    @Test
    void countAll() {
        GameManager game1 = new GameManagerImpl(1L, null, null, null);
        GameManager game2 = new GameManagerImpl(2L, null, null, null);
        GameManager game3 = new GameManagerImpl(3L, null, null, null);
        GameManager game4 = new GameManagerImpl(4L, null, null, null);
        repository.save(game1);
        repository.save(game2);
        repository.save(game3);
        repository.save(game4);

        assertEquals(4, repository.countAll());
    }

    @Test
    void saveDuplicate() {
        GameManager game1 = new GameManagerImpl(1L, null, null, null);
        GameManager game2 = new GameManagerImpl(2L, null, null, null);
        GameManager game3 = new GameManagerImpl(3L, null, null, null);
        GameManager game4 = new GameManagerImpl(3L, null, null, null);
        repository.save(game1);
        repository.save(game2);
        repository.save(game3);
        repository.save(game4);

        assertEquals(3, repository.countAll());
    }

    @Test
    void deleteGameById() {
        GameManager game1 = new GameManagerImpl(1L, null, null, null);
        GameManager game2 = new GameManagerImpl(2L, null, null, null);
        GameManager game3 = new GameManagerImpl(3L, null, null, null);
        GameManager game4 = new GameManagerImpl(4L, null, null, null);
        repository.save(game1);
        repository.save(game2);
        repository.save(game3);
        repository.save(game4);

        repository.deleteGameById(3L);

        assertEquals(3, repository.countAll());
        assertFalse(repository.containsKey(3L));
    }
}
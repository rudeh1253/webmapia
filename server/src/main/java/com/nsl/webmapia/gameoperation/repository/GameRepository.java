package com.nsl.webmapia.gameoperation.repository;

import com.nsl.webmapia.gameoperation.domain.GameManager;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    /**
     * Save game data into the storage.
     * @param game object to save.
     */
    void save(GameManager game);

    /**
     * Find and return game data from the storage,
     * by id of the game.
     * @param gameId of game to find.
     * @return game whose id matches the parameter. The Optional object is allowed to be empty.
     */
    Optional<GameManager> findById(Long gameId);

    /**
     * Find every game registered.
     * @return all the games.
     */
    List<GameManager> findAll();

    /**
     * Determine whether the game of the gameId exists.
     * @param gameId to check
     * @return true if the game of the id exists, otherwise false.
     */
    boolean containsKey(Long gameId);

    /**
     * Count the number of game stored in the repository.
     * @return the number of game.
     */
    int countAll();

    /**
     * Delete the game of gameId from the repository.
     * @param gameId of the game to delete.
     * @return the game deleted.
     */
    Optional<GameManager> deleteGameById(Long gameId);
}

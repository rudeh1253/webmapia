package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;

public interface GameService extends GameManager {

    /**
     * Create a new game.
     * @return gameId.
     */
    Long createNewGame();
}

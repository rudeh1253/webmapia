package com.nsl.webmapia.game.service;

public interface GameService {

    /**
     * This method is called when a game is start.
     */
    void onStart();

    /**
     * The phase of game steps forward.
     */
    void stepForward();


}

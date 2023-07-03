package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.PrivateNotificationBody;

import java.util.List;

public interface GameService {

    /**
     * This method is called when a game is start.
     */
    void onStart();

    /**
     * Each user get to have a character. Each user instance is mutated in this method.
     * @return information to be sent to each user saying that which character the user is allocated.
     */
    List<PrivateNotificationBody<Character>> allocateCharacterToEachUser();

    /**
     * The phase of game steps forward.
     */
    void stepForward();

    void addUser(Long userId);

    Long removeUser(Long userId);
}

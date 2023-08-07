package com.nsl.webmapia.user.service;

import com.nsl.webmapia.user.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    /**
     * Add user in the game.
     */
    void addUser(Long roomId, Long userId);

    /**
     * Add user in the game.
     */
    void addUser(Long roomId, Long userId, String userName);

    /**
     * Remove user from the game. If the user which is supposed to be removed doesn't exist in the game,
     * this will throw an exception.
     * @param roomId id of the game.
     * @param userId of user to remove.
     * @return User object which is removed, if it doesn't exist, throws.
     */
    UserResponseDTO removeUser(Long roomId, Long userId);

    /**
     * Return all users belongs to the game of gameId.
     * @param gameId id of game.
     * @return list of users belonging to the given game.
     */
    List<UserResponseDTO> getAllUsers(Long gameId);

    UserResponseDTO getUser(Long gameId, Long userId);

    Long generateId();

    void removeId(Long id);
}

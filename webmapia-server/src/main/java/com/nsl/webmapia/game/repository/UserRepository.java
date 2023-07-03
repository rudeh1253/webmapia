package com.nsl.webmapia.game.repository;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.CharacterCode;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    /**
     * Save user data into the storage.
     * @param user object to save.
     */
    void save(User user);

    /**
     * Find and return user data from the storage,
     * by id of the user.
     * @param userId of user to find.
     * @return user whose id matches the parameter. The Optional object is allowed to be empty.
     */
    Optional<User> findById(Long userId);

    /**
     * Find users by isDead and return them as a List object.
     * @param isDead if true, find users who are dead, otherwise alive.
     * @return list of users, each of which is dead or alive.
     */
    List<User> findByIsDead(boolean isDead);

    /**
     * Find users by characterCode and return them as a List object.
     * @param characterCode the user to find is holding.
     * @return list of users, each of which has character code is equal to the parameter.
     */
    List<User> findByCharacterCode(CharacterCode characterCode);

    /**
     * Find every user registered.
     * @return all the users.
     */
    List<User> findAll();

    /**
     * Determine whether the user of the id userId exists.
     * @param userId to check
     * @return true if the user of the id exists, otherwise false.
     */
    boolean containsKey(Long userId);

    /**
     * Count the number of user stored in the repository.
     * @return the number of user.
     */
    int countAll();

    /**
     * Delete the user of userId from the repository.
     * @param userId of the user to delete.
     * @return the user deleted.
     */
    User deleteUserById(Long userId);
}

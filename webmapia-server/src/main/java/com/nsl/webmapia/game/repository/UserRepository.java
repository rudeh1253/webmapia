package com.nsl.webmapia.game.repository;

import com.nsl.webmapia.game.domain.User;

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
     * Find every user registered.
     * @return all the users.
     */
    List<User> findAll();
}

package com.nsl.webmapia.game.repository;

import com.nsl.webmapia.game.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUserRepositoryTest {
    MemoryUserRepository repository = new MemoryUserRepository();

    @AfterEach
    void clearStorage() {
        repository.clear();
    }

    @Test
    void findByIdSuccess() {
        Long id = 1L;
        User user = new User(id);

        repository.save(user);

        Optional<User> result = repository.findById(id);

        Assertions.assertThat(result.get()).isEqualTo(user);
        Assertions.assertThat(result.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByIdFail() {
        User user = new User(1L);

        repository.save(user);

        Optional<User> result = repository.findById(2L);

        Assertions.assertThat(result.isEmpty()).isEqualTo(true);
    }

    @Test
    void findByIsDead_findDead() {
        User user1 = new User(1L);
        user1.setDead(true);
        repository.save(user1);

        User user2 = new User(2L);
        user2.setDead(false);
        repository.save(user2);

        User user3 = new User(3L);
        user3.setDead(true);
        repository.save(user3);

        User user4 = new User(4L);
        user4.setDead(false);
        repository.save(user4);

        List<User> result = repository.findByIsDead(true);
        result.stream()
                .forEach((elem) -> Assertions.assertThat(elem.isDead()).isEqualTo(true));
        result.stream()
                .forEach((elem) -> Assertions.assertThat(elem.getId()).isNotEqualTo(2L));
        result.stream()
                .forEach((elem) -> Assertions.assertThat(elem.getId()).isNotEqualTo(4L));
    }

    @Test
    void findByIsDead_findAlive() {
        User user1 = new User(1L);
        user1.setDead(true);
        repository.save(user1);

        User user2 = new User(2L);
        user2.setDead(false);
        repository.save(user2);

        User user3 = new User(3L);
        user3.setDead(true);
        repository.save(user3);

        User user4 = new User(4L);
        user4.setDead(false);
        repository.save(user4);

        List<User> result = repository.findByIsDead(false);
        result.stream()
                .forEach((elem) -> Assertions.assertThat(elem.isDead()).isEqualTo(false));
        result.stream()
                .forEach((elem) -> Assertions.assertThat(elem.getId()).isNotEqualTo(1L));
        result.stream()
                .forEach((elem) -> Assertions.assertThat(elem.getId()).isNotEqualTo(3L));
    }

    @Test
    void findAll() {
        User user1 = new User(1L);
        repository.save(user1);

        User user2 = new User(2L);
        repository.save(user2);

        User user3 = new User(3L);
        repository.save(user3);

        User user4 = new User(4L);
        repository.save(user4);

        List<User> result = repository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(4);
    }
}
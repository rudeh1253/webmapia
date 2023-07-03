package com.nsl.webmapia.game.repository;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryUserRepository implements UserRepository {
    private final Map<Long, User> storage = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        storage.put(user.getID(), user);
    }

    @Override
    public Optional<User> findById(Long userId) {
        User result = storage.get(userId);
        return Optional.ofNullable(result);
    }

    @Override
    public List<User> findByIsDead(boolean isDead) {
        return storage.values().stream()
                .filter((user) -> user.isDead() == isDead)
                .toList();
    }

    @Override
    public List<User> findByCharacterCode(CharacterCode characterCode) {
        return storage.values().stream()
                .filter(user -> user.getCharacter().getCharacterCode() == characterCode)
                .toList();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean containsKey(Long userId) {
        return storage.containsKey(userId);
    }

    @Override
    public int countAll() {
        return storage.size();
    }

    @Override
    public User deleteUserById(Long userId) {
        return storage.remove(userId);
    }

    public void clear() {
        storage.clear();
    }
}

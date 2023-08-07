package com.nsl.webmapia.user.repository;

import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryUserIdRepository implements UserIdRepository {
    private static final Set<Long> idSet = ConcurrentHashMap.newKeySet();

    @Override
    public void addId(Long id) {
        idSet.add(id);
    }

    @Override
    public boolean exist(Long id) {
        return idSet.contains(id);
    }

    @Override
    public void remove(Long id) {
        idSet.remove(id);
    }
}

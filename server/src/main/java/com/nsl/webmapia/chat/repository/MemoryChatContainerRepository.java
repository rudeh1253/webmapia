package com.nsl.webmapia.chat.repository;

import com.nsl.webmapia.chat.domain.ChatContainer;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryChatContainerRepository implements ChatContainerRepository {

    // Key: Game id
    // Value:
    //     Key: Container id
    //     Value: ChatContainer instance
    private final Map<Long, Map<Long, ChatContainer>> store;

    public MemoryChatContainerRepository() {
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public void save(ChatContainer newContainer) {
        Map<Long, ChatContainer> containerMap = store.get(newContainer.getGameId());
        if (containerMap == null) {
            Map<Long, ChatContainer> newContainerMap = new ConcurrentHashMap<>();
            newContainerMap.put(newContainer.getContainerId(), newContainer);
            store.put(newContainer.getGameId(), newContainerMap);
        } else {
            containerMap.put(newContainer.getContainerId(), newContainer);
        }
    }

    @Override
    public void deleteByGameIdAndContainerId(Long gameId, Long containerId) {
        Map<Long, ChatContainer> containerMap = store.get(gameId);
        if (containerMap != null) {
            containerMap.remove(containerId);
        }
    }

    @Override
    public void deleteAllByGameId(Long gameId) {
        store.remove(gameId);
    }

    @Override
    public Map<Long, ChatContainer> findAllByGameId(Long gameId) throws NoSuchElementException {
        Map<Long, ChatContainer> containerMap = store.get(gameId);
        if (containerMap == null) {
            throw new NoSuchElementException("No such element");
        }
        return containerMap;
    }

    @Override
    public Optional<ChatContainer> findByGameIdAndContainerId(Long gameId, Long containerId) {
        Map<Long, ChatContainer> containerMap = this.store.get(gameId);
        System.out.println("gameId: " + gameId);
        System.out.println("containerId: " + containerId);
        System.out.println("Store: " + store);
        if (containerMap != null) {
            ChatContainer chatContainer = containerMap.get(containerId);
            return Optional.ofNullable(chatContainer);
        } else {
            return Optional.empty();
        }
    }
}

package com.nsl.webmapia.chat.repository;

import com.nsl.webmapia.chat.domain.ChatContainer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatContainerRepository {
    void save(ChatContainer newContainer);
    void deleteByGameIdAndContainerId(Long gameId, Long containerId);
    void deleteAllByGameId(Long gameId);
    Map<Long, ChatContainer> findAllByGameId(Long gameId);
    Optional<ChatContainer> findByGameIdAndContainerId(Long gameId, Long containerId);
}

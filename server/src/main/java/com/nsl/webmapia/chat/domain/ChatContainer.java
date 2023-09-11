package com.nsl.webmapia.chat.domain;

import com.nsl.webmapia.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class ChatContainer {
    private final Long gameId;
    private final Long containerId;
    private final String containerName;
    private final List<Long> participantIds;

    public ChatContainer(Long gameId, Long containerId, String containerName) {
        this.gameId = gameId;
        this.containerId = containerId;
        this.containerName = containerName;
        this.participantIds = new ArrayList<>();
    }

    public ChatContainer(Long gameId, Long containerId, String containerName, Long participantId) {
        this.gameId = gameId;
        this.containerId = containerId;
        this.containerName = containerName;
        this.participantIds = new ArrayList<>();
        this.participantIds.add(participantId);
    }

    public List<Long> getParticipantIds() {
        List<Long> toReturn = new ArrayList<>();
        toReturn.addAll(participantIds);
        return toReturn;
    }

    public void addParticipant(Long newParticipantId) {
        this.participantIds.add(newParticipantId);
    }
}

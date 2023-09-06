package com.nsl.webmapia.chat.domain;

import com.nsl.webmapia.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatContainer {
    private final Long gameId;
    private final Long containerId;
    private final String containerName;
    private final List<Long> participantIds;

    public List<Long> getParticipantIds() {
        List<Long> toReturn = new ArrayList<>();
        Collections.copy(toReturn, participantIds);
        return toReturn;
    }
}

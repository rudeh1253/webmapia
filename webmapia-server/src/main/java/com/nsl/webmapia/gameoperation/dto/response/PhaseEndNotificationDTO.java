package com.nsl.webmapia.gameoperation.dto.response;

import com.nsl.webmapia.common.NotificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class PhaseEndNotificationDTO {
    private NotificationType notificationType;
    private Long gameId;
    private boolean isEnd;

    public static PhaseEndNotificationDTO from(Long gameId, boolean isEnd) {
        return PhaseEndNotificationDTO.builder()
                .notificationType(NotificationType.PHASE_END)
                .gameId(gameId)
                .isEnd(isEnd)
                .build();
    }
}

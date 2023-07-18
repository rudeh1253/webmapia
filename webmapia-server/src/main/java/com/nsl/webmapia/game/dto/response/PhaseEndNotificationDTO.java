package com.nsl.webmapia.game.dto.response;

import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class PhaseEndNotificationDTO {
    private GameNotificationType notificationType;
    private Long gameId;
    private boolean isEnd;

    public static PhaseEndNotificationDTO from(Long gameId, boolean isEnd) {
        return PhaseEndNotificationDTO.builder()
                .notificationType(GameNotificationType.PHASE_END)
                .gameId(gameId)
                .isEnd(isEnd)
                .build();
    }
}

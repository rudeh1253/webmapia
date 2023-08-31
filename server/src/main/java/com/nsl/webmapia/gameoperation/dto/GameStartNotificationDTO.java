package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GameSetting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class GameStartNotificationDTO {
    private final NotificationType notificationType;
    private final GameSetting gameSetting;
    private final Long gameId;

    public GameStartNotificationDTO from(GameSetting gameSetting, Long gameId) {
        return GameStartNotificationDTO.builder()
                .notificationType(NotificationType.GAME_START)
                .gameSetting(gameSetting)
                .gameId(gameId)
                .build();
    }
}

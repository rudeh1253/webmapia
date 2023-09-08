package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GameSetting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class GameStartResponseDTO {
    private final NotificationType notificationType;
    private final GameSetting gameSetting;
    private final Long gameId;

    public static GameStartResponseDTO from(GameSetting gameSetting, Long gameId) {
        return GameStartResponseDTO.builder()
                .notificationType(NotificationType.GAME_START)
                .gameSetting(gameSetting)
                .gameId(gameId)
                .build();
    }
}

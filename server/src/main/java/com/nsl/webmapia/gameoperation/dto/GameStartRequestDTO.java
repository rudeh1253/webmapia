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
public class GameStartRequestDTO {
    private final GameSetting gameSetting;
    private final Long gameId;

    public GameStartRequestDTO from(GameSetting gameSetting, Long gameId) {
        return GameStartRequestDTO.builder()
                .gameSetting(gameSetting)
                .gameId(gameId)
                .build();
    }
}

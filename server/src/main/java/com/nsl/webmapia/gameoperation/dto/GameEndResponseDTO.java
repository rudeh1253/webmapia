package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.common.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameEndResponseDTO {
    private NotificationType notificationType;
    private Long gameId;

    public GameEndResponseDTO(Long gameId) {
        this.notificationType = NotificationType.GAME_END;
        this.gameId = gameId;
    }
}

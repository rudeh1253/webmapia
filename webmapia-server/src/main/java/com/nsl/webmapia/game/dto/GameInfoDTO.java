package com.nsl.webmapia.game.dto;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
public class GameInfoDTO {
    private final GameNotificationType notificationType;
    private final Long gameId;
    private final List<UserResponseDTO> users;

    public static GameInfoDTO from(GameManager gameManager) {
        List<UserResponseDTO> userResponseDTOs = gameManager.getAllUsers().stream()
                .map(user -> UserResponseDTO.from(GameNotificationType.GAME_INFO, gameManager.getGameId(), user))
                .toList();
        return GameInfoDTO.builder()
                .gameId(gameManager.getGameId())
                .notificationType(GameNotificationType.GAME_INFO)
                .users(userResponseDTOs)
                .build();
    }
}

package com.nsl.webmapia.game.dto.response;

import com.nsl.webmapia.game.domain.management.GameManager;
import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
public class GameInfoResponseDTO {
    private final GameNotificationType notificationType;
    private final Long gameId;
    private final Long hostId;
    private final String gameName;
    private final List<UserResponseDTO> users;

    public static GameInfoResponseDTO from(GameManager gameManager) {
        List<UserResponseDTO> userResponseDTOs = gameManager.getAllUsers().stream()
                .map(user -> UserResponseDTO.from(GameNotificationType.GAME_INFO, gameManager.getGameId(), user))
                .toList();
        return GameInfoResponseDTO.builder()
                .gameId(gameManager.getGameId())
                .notificationType(GameNotificationType.GAME_INFO)
                .hostId(gameManager.getHost().getID())
                .gameName(gameManager.getGameName())
                .users(userResponseDTOs)
                .build();
    }
}

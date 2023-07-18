package com.nsl.webmapia.room.dto;

import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.user.dto.UserResponseDTO;
import com.nsl.webmapia.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
public class RoomInfoResponseDTO {
    private final NotificationType notificationType;
    private final Long gameId;
    private final Long hostId;
    private final String gameName;
    private final List<UserResponseDTO> users;

    public static RoomInfoResponseDTO from(GameManager gameManager) {
        List<UserResponseDTO> userResponseDTOs = gameManager.getAllUsers().stream()
                .map(user -> UserResponseDTO.from(NotificationType.GAME_INFO, gameManager.getGameId(), user))
                .toList();
        User host = gameManager.getHost();
        return RoomInfoResponseDTO.builder()
                .gameId(gameManager.getGameId())
                .notificationType(NotificationType.GAME_INFO)
                .hostId(host != null ? host.getID() : null)
                .gameName(gameManager.getGameName())
                .users(userResponseDTOs)
                .build();
    }
}

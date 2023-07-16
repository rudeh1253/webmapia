package com.nsl.webmapia.game.dto;

import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
public class UserRequestDTO {
    private final GameNotificationType notificationType;
    private final Long gameId;
    private final Long userId;
    private final String userName;
}

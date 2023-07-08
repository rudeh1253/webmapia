package com.nsl.webmapia.game.dto;

import com.nsl.webmapia.game.domain.notification.GameNotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDTO {
    private final GameNotificationType gameNotificationType;
    private final Long gameId;
    private final Long receiverUserId;
    private final Long targetUserId;
}

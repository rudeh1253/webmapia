package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.character.CharacterCode;
import lombok.*;

@Getter
@AllArgsConstructor
public class PublicNotificationBody<T> {
    private final NotificationType notificationType;
    private final Long targetUserId;
    private final T data;
}

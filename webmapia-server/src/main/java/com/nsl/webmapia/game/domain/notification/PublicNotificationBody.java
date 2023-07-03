package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.character.CharacterCode;
import lombok.*;

@Setter
@Getter
public class PublicNotificationBody<T> {
    private NotificationType notificationType;
    private Long targetUserId;
    private T data;

    @Builder
    public PublicNotificationBody(NotificationType notificationType, Long targetUserId, T data) {
        this.notificationType = notificationType;
        this.targetUserId = targetUserId;
        this.data = data;
    }
}

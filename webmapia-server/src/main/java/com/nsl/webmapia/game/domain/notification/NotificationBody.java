package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.User;
import lombok.*;

@Setter
@Getter
public class NotificationBody<T> {
    private NotificationType notificationType;
    private User receiver;
    private T data;

    @Builder
    public NotificationBody(NotificationType notificationType, User receiver, T data) {
        this.notificationType = notificationType;
        this.receiver = receiver;
        this.data = data;
    }
}

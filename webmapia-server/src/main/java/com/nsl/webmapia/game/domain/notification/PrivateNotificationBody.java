package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class PrivateNotificationBody <T> {
    private final NotificationType notificationType;
    private final User receiver;
    private final T data;
}

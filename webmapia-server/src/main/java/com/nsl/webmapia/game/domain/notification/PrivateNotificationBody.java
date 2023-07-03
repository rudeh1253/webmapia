package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrivateNotificationBody <T> {
    private NotificationType notificationType;
    private User receiver;
    private T data;
}

package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.notification.NotificationBody;

import java.util.List;

public interface PublicNotificationService {

    <T> void addNotification(NotificationBody<T> publicNotification);

    <T> List<NotificationBody<T>> broadcast();
}

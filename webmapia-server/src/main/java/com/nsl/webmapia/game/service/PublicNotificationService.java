package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.notification.GameNotificationBody;

import java.util.List;

public interface PublicNotificationService {

    <T> void addNotification(GameNotificationBody<T> publicNotification);

    <T> List<GameNotificationBody<T>> broadcast();
}

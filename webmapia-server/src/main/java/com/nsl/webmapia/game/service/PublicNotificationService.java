package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.notification.GameNotification;

import java.util.List;

public interface PublicNotificationService {

    <T> void addNotification(GameNotification<T> publicNotification);

    <T> List<GameNotification<T>> broadcast();
}

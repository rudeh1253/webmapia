package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.notification.PublicNotificationBody;

import java.util.List;

public interface PublicNotificationService {

    <T> void addNotification(PublicNotificationBody<T> publicNotification);

    <T> List<PublicNotificationBody<T>> broadcast();
}

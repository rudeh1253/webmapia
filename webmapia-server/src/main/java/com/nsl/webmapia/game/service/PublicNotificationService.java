package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.notification.PublicNotificationBody;

import java.util.List;

public interface PublicNotificationService {

    void addNotification(PublicNotificationBody publicNotification);

    List<PublicNotificationBody> broadcast();
}

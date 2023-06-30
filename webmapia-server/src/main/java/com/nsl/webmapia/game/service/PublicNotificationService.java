package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.User;

import java.util.List;

public interface PublicNotificationService {

    void addNotification(String message);

    List<String> broadcast();
}

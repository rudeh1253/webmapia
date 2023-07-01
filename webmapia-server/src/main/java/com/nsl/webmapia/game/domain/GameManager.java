package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.notification.SkillNotificationBody;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameManager {
    private final List<SkillNotificationBody> skillNotifications;

    public GameManager() {
        this.skillNotifications = new ArrayList<>();
    }

    public void addSkillNotification(SkillNotificationBody skillNotification) {
        skillNotifications.add(skillNotification);
    }
}

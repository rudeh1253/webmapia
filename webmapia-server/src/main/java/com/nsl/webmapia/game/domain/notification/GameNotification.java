package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.User;
import lombok.*;

@Setter
@Getter
public class GameNotification<T> {
    private GameNotificationType gameNotificationType;
    private User receiver;
    private T data;
    private Long gameId;

    @Builder
    public GameNotification(GameNotificationType gameNotificationType, User receiver, T data, Long gameId) {
        this.gameNotificationType = gameNotificationType;
        this.receiver = receiver;
        this.data = data;
        this.gameId = gameId;
    }
}

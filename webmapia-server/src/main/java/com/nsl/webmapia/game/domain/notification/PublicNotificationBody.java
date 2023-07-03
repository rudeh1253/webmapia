package com.nsl.webmapia.game.domain.notification;

import com.nsl.webmapia.game.domain.character.CharacterCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PublicNotificationBody {
    private NotificationType notificationType;
    private Long targetUserId;
    private CharacterCode targetUserCharacterCode;

    @Builder
    public PublicNotificationBody(NotificationType notificationType, Long targetUserId, CharacterCode targetUserCharacterCode) {
        this.notificationType = notificationType;
        this.targetUserId = targetUserId;
        this.targetUserCharacterCode = targetUserCharacterCode;
    }
}

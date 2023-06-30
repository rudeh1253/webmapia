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
    private PublicNotificationType publicNotificationType;
    private Long targetUserId;
    private CharacterCode targetUserCharacterCode;

    @Builder
    public PublicNotificationBody(PublicNotificationType publicNotificationType, Long targetUserId, CharacterCode targetUserCharacterCode) {
        this.publicNotificationType = publicNotificationType;
        this.targetUserId = targetUserId;
        this.targetUserCharacterCode = targetUserCharacterCode;
    }
}

package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.character.CharacterCode;
import com.nsl.webmapia.common.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
@ToString
public class CharacterGenerationResponseDTO {
    private final NotificationType notificationType;
    private final Long receiverId;
    private final CharacterCode characterCode;
    private final Long gameId;

    private CharacterGenerationResponseDTO(NotificationType notificationType, Long receiverId, CharacterCode characterCode, Long gameId) {
        this.notificationType = notificationType;
        this.receiverId = receiverId;
        this.characterCode = characterCode;
        this.gameId = gameId;
    }

    public static CharacterGenerationResponseDTO from(NotificationType notificationType,
                                                      Long receiverId,
                                                      CharacterCode characterCode,
                                                      Long gameId) {
        return CharacterGenerationResponseDTO.builder()
                .notificationType(notificationType)
                .receiverId(receiverId)
                .characterCode(characterCode)
                .gameId(gameId)
                .build();
    }
}

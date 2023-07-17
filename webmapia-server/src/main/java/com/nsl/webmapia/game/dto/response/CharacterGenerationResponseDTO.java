package com.nsl.webmapia.game.dto.response;

import com.nsl.webmapia.game.domain.character.CharacterCode;
import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
public class CharacterGenerationResponseDTO {
    private final GameNotificationType notificationType;
    private final Long receiverId;
    private final CharacterCode characterCode;
    private final Long gameId;

    private CharacterGenerationResponseDTO(GameNotificationType notificationType, Long receiverId, CharacterCode characterCode, Long gameId) {
        this.notificationType = notificationType;
        this.receiverId = receiverId;
        this.characterCode = characterCode;
        this.gameId = gameId;
    }

    public static CharacterGenerationResponseDTO from(GameNotificationType notificationType,
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

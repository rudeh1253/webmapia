package com.nsl.webmapia.game.dto;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
@Builder(access = PRIVATE)
public class UserResponseDTO {
    private final GameNotificationType notificationType;
    private final Long gameId;
    private final Long userId;
    private final String userName;
    private final CharacterCode characterCode;
    private final boolean isDead;

    public static UserResponseDTO from(GameNotificationType notificationType, Long gameId, User user) {
        CharacterCode characterCode = user.getCharacter() == null ? null : user.getCharacter().getCharacterCode();
        return UserResponseDTO.builder()
                .notificationType(notificationType)
                .gameId(gameId)
                .userId(user.getID())
                .userName(user.getName())
                .characterCode(characterCode)
                .isDead(user.isDead())
                .build();
    }
}

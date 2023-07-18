package com.nsl.webmapia.game.dto.response;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder(access = PRIVATE)
@ToString
public class UserResponseDTO {
    private GameNotificationType notificationType;
    private Long gameId;
    private Long userId;
    private String username;
    private CharacterCode characterCode;
    private boolean isDead;

    public static UserResponseDTO from(GameNotificationType notificationType, Long gameId, User user) {
        CharacterCode characterCode = user.getCharacter() == null ? null : user.getCharacter().getCharacterCode();
        return UserResponseDTO.builder()
                .notificationType(notificationType)
                .gameId(gameId)
                .userId(user.getID())
                .username(user.getName())
                .characterCode(characterCode)
                .isDead(user.isDead())
                .build();
    }
}

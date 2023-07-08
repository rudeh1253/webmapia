package com.nsl.webmapia.game.dto;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
@Builder(access = PRIVATE)
public class UserResponseDTO {
    private final Long gameId;
    private final Long userId;
    private final String userName;
    private final CharacterCode characterCode;
    private final boolean isDead;

    public static UserResponseDTO from(Long gameId, User user) {
        return UserResponseDTO.builder()
                .gameId(gameId)
                .userId(user.getID())
                .userName(user.getName())
                .characterCode(user.getCharacter().getCharacterCode())
                .isDead(user.isDead())
                .build();
    }
}

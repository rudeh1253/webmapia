package com.nsl.webmapia.game.dto.request;

import com.nsl.webmapia.game.domain.GameNotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserRequestDTO {
    private GameNotificationType notificationType;
    private Long gameId;
    private Long userId;
    private String username;
}

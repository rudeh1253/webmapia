package com.nsl.webmapia.user.dto;

import com.nsl.webmapia.common.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserRequestDTO {
    private NotificationType notificationType;
    private Long gameId;
    private Long userId;
    private String username;
}

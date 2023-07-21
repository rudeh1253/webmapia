package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.common.NotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PhaseEndRequestDTO {
    private final NotificationType notificationType;
    private final Long gameId;
    private final Long userId;
}

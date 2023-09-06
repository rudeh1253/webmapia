package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.common.NotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class PhaseEndRequestDTO {
    private final Long gameId;
    private final Long userId;
}

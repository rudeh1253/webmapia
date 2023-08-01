package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.common.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class VoteRequestDTO {
    private final NotificationType notificationType;
    private final Long gameId;
    private final Long voterId;
    private final Long subjectId;
}

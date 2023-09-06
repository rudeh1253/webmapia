package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.common.NotificationType;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class VoteRequestDTO {
    private final NotificationType notificationType;
    private final Long gameId;
    private final Long voterId;
    private final Long subjectId;
}

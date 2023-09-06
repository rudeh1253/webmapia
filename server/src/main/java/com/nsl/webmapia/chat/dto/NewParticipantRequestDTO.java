package com.nsl.webmapia.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NewParticipantRequestDTO {
    private Long gameId;
    private Long containerId;
    private Long userId;
}

package com.nsl.webmapia.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ParticipateChatContainerRequestDTO {
    private Long gameId;
    private Long containerId;
    private String containerName;
    private Long participant;
}

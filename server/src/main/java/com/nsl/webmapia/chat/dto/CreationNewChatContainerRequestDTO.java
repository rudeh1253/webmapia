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
public class CreationNewChatContainerRequestDTO {
    private Long gameId;
    private String containerName;
    private List<Long> usersToGetIn;
}

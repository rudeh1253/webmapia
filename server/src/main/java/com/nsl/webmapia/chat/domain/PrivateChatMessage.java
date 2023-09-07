package com.nsl.webmapia.chat.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrivateChatMessage {
    private Long gameId;
    private Long senderId;
    private String message;
    private Long containerId;
}

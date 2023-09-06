package com.nsl.webmapia.chat.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrivateChatMessage extends PublicChatMessage {
    Long containerId;
}

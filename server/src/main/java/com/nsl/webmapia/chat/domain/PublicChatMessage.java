package com.nsl.webmapia.chat.domain;

import lombok.*;

/**
 * A Message model class. This class defines a message data
 * sent between each endpoint.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PublicChatMessage {
    private Long gameId;
    private Long senderId;
    private String message;
}
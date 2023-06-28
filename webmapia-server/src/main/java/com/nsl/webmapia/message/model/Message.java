package com.nsl.webmapia.message.model;

import lombok.*;

import java.util.Date;

/**
 * A Message model class. This class defines a message data
 * sent between each endpoint.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private Date date;
    private Status status;
}
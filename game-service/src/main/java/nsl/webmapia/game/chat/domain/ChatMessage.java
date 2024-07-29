package nsl.webmapia.game.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ChatMessage {
    private Integer messageId;
    private String message;
    private LocalDateTime pubTime;
    private String senderId;
}

package nsl.webmapia.game.common;

import java.util.List;

public class BaseSystemMessageResponseDto<D> {
    private List<String> receiverIds;
    private SystemMessageType systemMessageType;
    private String message;
    private D content;
}

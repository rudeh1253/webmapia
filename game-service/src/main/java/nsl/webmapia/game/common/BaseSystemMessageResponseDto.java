package nsl.webmapia.game.common;

import java.util.List;

/**
 * This class describe the object that the message format sent from server.
 * This is used in the context of sending message over WebSocket protocol from server
 * to client. Unlike traditional HTTP, there can exist a case that WebSocket server sends
 * data to clients without receiving any request. In this case, the message can be a system message.
 * The server has to recognize receivers, and recognizing receivers will be processed in Service layer.
 * <code>receiverIds</code> is a list of ids of receivers who will receive this message.
 *
 * @param <D> type of data to be sent
 */
public class BaseSystemMessageResponseDto<D> {
    private List<String> receiverIds;
    private SystemMessageType systemMessageType;
    private String message;
    private D content;
}

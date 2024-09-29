package nsl.webmapia.game.gameroom.exception;

import nsl.webmapia.game.common.ErrorCode;

public class UnableToEnterGameRoomException extends RuntimeException {
    private final ErrorCode errorCode;

    public UnableToEnterGameRoomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public UnableToEnterGameRoomException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
}

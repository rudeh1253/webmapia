package nsl.webmapia.game.domain.gameroom.exception;

import nsl.webmapia.game.global.ErrorCode;

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

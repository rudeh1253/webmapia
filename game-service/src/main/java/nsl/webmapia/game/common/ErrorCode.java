package nsl.webmapia.game.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    GAME_ALREADY_STARTED(202, "Game has already started");

    private final int statusCode;
    private final String message;

    public int statusCode() {
        return this.statusCode;
    }

    public String message() {
        return this.message;
    }
}

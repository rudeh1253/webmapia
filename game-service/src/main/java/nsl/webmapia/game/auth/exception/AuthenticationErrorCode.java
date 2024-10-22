package nsl.webmapia.game.auth.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthenticationErrorCode {
    AUTHENTICATION_FAILURE("A000", 40100, "Authentication failed"),
    MEMBER_NOT_FOUND("A001", 40101, "Member not found"),
    ACCESS_DENIED("A002", 40300, "You are not authorized");

    private final String errorCode;
    private final int statusCode;
    private final String message;

    public String errorCode() {
        return this.errorCode;
    }

    public int statusCode() {
        return this.statusCode;
    }

    public String message() {
        return this.message;
    }
}

package nsl.webmapia.game.auth.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class UsernameAuthenticationException extends AuthenticationException {
    private final Object username;

    public UsernameAuthenticationException(Object username, String msg, Throwable cause) {
        super(msg, cause);
        this.username = username;
    }

    public UsernameAuthenticationException(Object username, String msg) {
        super(msg);
        this.username = username;
    }
}

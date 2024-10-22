package nsl.webmapia.game.auth.beans;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoEncodingPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}

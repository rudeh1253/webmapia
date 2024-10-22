package nsl.webmapia.game.auth.beans;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class TestNoEncodingPasswordEncoder {

    PasswordEncoder passwordEncoder = new NoEncodingPasswordEncoder();

    @Test
    void encode() {
        String rawPassword = "1q2w3e4r!@";
        String result = this.passwordEncoder.encode(rawPassword);
        assertThat(result).isEqualTo(rawPassword);
    }

    @Test
    void matches() {
        String rawPassword = "1q2w3e4r!@";
        assertThat(this.passwordEncoder.matches(rawPassword, rawPassword)).isTrue();
    }
}
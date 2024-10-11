package nsl.webmapia.authserver.security.password;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestNoEncryptionPasswordEncoder {
    NoEncryptionPasswordEncoder passwordEncoder = new NoEncryptionPasswordEncoder();

    @Test
    void encode() {
        String rawPassword = "1q2w3e4r";
        String result = this.passwordEncoder.encode(rawPassword);
        assertThat(result).isEqualTo(rawPassword);
    }

    @Test
    void matches() {
        String rawPassword = "1q2w3e4r";
        boolean result = this.passwordEncoder.matches(rawPassword, this.passwordEncoder.encode(rawPassword));
        assertThat(result).isTrue();
    }
}
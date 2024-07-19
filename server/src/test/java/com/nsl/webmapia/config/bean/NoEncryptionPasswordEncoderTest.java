package com.nsl.webmapia.config.bean;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles
public class NoEncryptionPasswordEncoderTest {

    @Qualifier("noEncryptionPasswordEncoder")
    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("Encoded password and raw password should be the same.")
    @ParameterizedTest
    @ValueSource(strings = { "1q2w3e4r", "asdf", "", "g428h103412", "123456789" })
    void checkPasswordEncoderDoesntEncryptAnyPassword(String rasPassword) {
        String encoded = this.passwordEncoder.encode(rasPassword);
        log.debug("rawPassword={}, encodedPassword={}", rasPassword, encoded);
        assertThat(this.passwordEncoder.matches(rasPassword, encoded)).isTrue();
    }
}

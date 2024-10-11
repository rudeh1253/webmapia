package nsl.webmapia.authserver.security.config;

import nsl.webmapia.authserver.security.password.NoEncryptionPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Profile("no-sec")
    @Bean
    public PasswordEncoder noEncryptPasswordEncoder() {
        return new NoEncryptionPasswordEncoder();
    }

    @Profile("!no-sec")
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

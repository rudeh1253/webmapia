package nsl.webmapia.game.auth.config;

import nsl.webmapia.game.auth.beans.NoEncodingPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class AuthBeanConfig {

    @Profile("no-auth")
    @Bean
    public PasswordEncoder noEncodingPasswordEncoder() {
        return new NoEncodingPasswordEncoder();
    }

    @Profile("!no-auth")
    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Profile("no-auth")
    @Bean
    public AuthenticationManager providerManager(List<AuthenticationProvider> authenticationProviders) {
        return new ProviderManager(authenticationProviders);
    }
}

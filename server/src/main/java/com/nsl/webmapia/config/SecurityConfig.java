package com.nsl.webmapia.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Get a Spring Bean of Password Encoder which doesn't execute any encryption to
     * passwords. This is for development.
     * @return a PasswordEncoder instance which deals with plain password text intact.
     */
    @ConditionalOnMissingBean(PasswordEncoder.class)
    @Bean
    public PasswordEncoder noEncryptionPasswordEncoder() {
        return new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                return String.valueOf(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return String.valueOf(rawPassword).equals(encodedPassword);
            }
        };
    }

    @ConditionalOnMissingBean(SecurityFilterChain.class)
    @Bean
    public SecurityFilterChain allPermitSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((registry) -> registry.anyRequest().permitAll())
                .build();
    }
}

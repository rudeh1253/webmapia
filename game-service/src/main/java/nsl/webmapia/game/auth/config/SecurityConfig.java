package nsl.webmapia.game.auth.config;

import nsl.webmapia.game.auth.authentication.entrypoint.DelegatingAuthenticationEntryPoint;
import nsl.webmapia.game.auth.authorization.DefaultAccessDeniedHandler;
import nsl.webmapia.game.auth.filter.NoAuthAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Profile("no-auth")
    @Bean
    public SecurityFilterChain noAuthFilterChain(HttpSecurity http,
                                                 AuthenticationManager authenticationManager,
                                                 DelegatingAuthenticationEntryPoint authenticationEntryPoint,
                                                 DefaultAccessDeniedHandler accessDeniedHandler) throws Exception {
        return http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests((request) ->
                        request.anyRequest().permitAll())
                .sessionManagement((s) -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .addFilterAfter(new NoAuthAuthenticationFilter(authenticationManager), ExceptionTranslationFilter.class)
                .exceptionHandling((customizer) -> customizer.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .build();
    }
}

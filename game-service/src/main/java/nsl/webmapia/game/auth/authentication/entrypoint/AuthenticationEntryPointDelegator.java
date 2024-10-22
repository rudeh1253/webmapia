package nsl.webmapia.game.auth.authentication.entrypoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public interface AuthenticationEntryPointDelegator<E extends AuthenticationException> {

    void commence(HttpServletRequest request, HttpServletResponse response, E authException) throws IOException, ServletException;

    Class<? extends AuthenticationException> getSupportingAuthenticationException();
}

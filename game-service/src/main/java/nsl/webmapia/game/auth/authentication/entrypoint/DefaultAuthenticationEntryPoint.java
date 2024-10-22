package nsl.webmapia.game.auth.authentication.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.auth.exception.AuthenticationErrorCode;
import nsl.webmapia.game.global.rest.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPointDelegator<AuthenticationException> {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(AuthenticationErrorCode.AUTHENTICATION_FAILURE.statusCode())
                .statusCodeSeries(4)
                .errorCode(AuthenticationErrorCode.AUTHENTICATION_FAILURE.errorCode())
                .errorName(AuthenticationErrorCode.AUTHENTICATION_FAILURE.name())
                .message(AuthenticationErrorCode.AUTHENTICATION_FAILURE.message())
                .build();
        this.objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    @Override
    public Class<AuthenticationException> getSupportingAuthenticationException() {
        return AuthenticationException.class;
    }
}

package nsl.webmapia.game.auth.authentication.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.auth.exception.AuthenticationErrorCode;
import nsl.webmapia.game.auth.exception.UsernameAuthenticationException;
import nsl.webmapia.game.global.rest.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsernameAuthenticationEntryPoint implements AuthenticationEntryPointDelegator<UsernameAuthenticationException> {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, UsernameAuthenticationException authException)
            throws IOException, ServletException {
        log.error("Authentication fail", authException);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(AuthenticationErrorCode.MEMBER_NOT_FOUND.statusCode())
                .statusCodeSeries(4)
                .errorCode(AuthenticationErrorCode.MEMBER_NOT_FOUND.errorCode())
                .errorName(AuthenticationErrorCode.MEMBER_NOT_FOUND.name())
                .message(AuthenticationErrorCode.MEMBER_NOT_FOUND.message())
                .content(Map.of("memberId", authException.getUsername()))
                .build();
        this.objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    @Override
    public Class<UsernameAuthenticationException> getSupportingAuthenticationException() {
        return UsernameAuthenticationException.class;
    }
}

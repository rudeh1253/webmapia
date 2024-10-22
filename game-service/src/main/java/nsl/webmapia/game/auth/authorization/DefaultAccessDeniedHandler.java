package nsl.webmapia.game.auth.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.auth.exception.AuthenticationErrorCode;
import nsl.webmapia.game.global.rest.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(AuthenticationErrorCode.ACCESS_DENIED.statusCode())
                .statusCodeSeries(4)
                .message(AuthenticationErrorCode.ACCESS_DENIED.message())
                .errorName(AuthenticationErrorCode.ACCESS_DENIED.name())
                .errorCode(AuthenticationErrorCode.ACCESS_DENIED.errorCode())
                .build();
        this.objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}

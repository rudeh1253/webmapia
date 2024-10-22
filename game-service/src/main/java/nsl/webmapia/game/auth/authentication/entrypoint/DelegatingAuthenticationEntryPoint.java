package nsl.webmapia.game.auth.authentication.entrypoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DelegatingAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Map<Class<? extends AuthenticationException>, AuthenticationEntryPointDelegator<?>> delegators;
    private final DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    public DelegatingAuthenticationEntryPoint(Collection<AuthenticationEntryPointDelegator<?>> delegators,
                                              DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint) {
        this.delegators = getDelegatorMap(delegators);
        this.defaultAuthenticationEntryPoint = defaultAuthenticationEntryPoint;
    }

    private Map<Class<? extends AuthenticationException>, AuthenticationEntryPointDelegator<?>> getDelegatorMap(
            Collection<AuthenticationEntryPointDelegator<?>> delegators) {
        return delegators.stream()
                .filter((delegator) -> delegator.getClass() != DefaultAuthenticationEntryPoint.class)
                .collect(Collectors.toMap(
                        AuthenticationEntryPointDelegator::getSupportingAuthenticationException,
                        (delegator) -> delegator
                ));
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Here");
        log.info("isCommitted={}", response.isCommitted());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AuthenticationEntryPointDelegator<?> delegator = this.delegators.get(authException.getClass());
        if (delegator != null) {
            try {
                Method commenceMethod =
                        delegator.getClass().getDeclaredMethod("commence", HttpServletRequest.class, HttpServletResponse.class, AuthenticationException.class);
                commenceMethod.invoke(delegator, request, response, authException);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.defaultAuthenticationEntryPoint.commence(request, response, authException);
        }
    }
}

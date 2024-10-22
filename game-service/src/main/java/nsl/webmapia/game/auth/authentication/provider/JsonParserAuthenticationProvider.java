package nsl.webmapia.game.auth.authentication.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.auth.authentication.SingleAuthorityAuthentication;
import nsl.webmapia.game.auth.authentication.BearerTokenAuthentication;
import nsl.webmapia.game.auth.exception.UsernameAuthenticationException;
import nsl.webmapia.game.domain.member.constant.Role;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.domain.member.repository.MemberRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("no-auth")
public class JsonParserAuthenticationProvider implements AuthenticationProvider {
    private final MemberRepository memberRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthentication bearerTokenAuthentication = (BearerTokenAuthentication)authentication;
        String username;
        try {
            JSONObject jsonObject = new JSONObject(bearerTokenAuthentication.getPrincipal());
            username = jsonObject.getString("memberId");
        } catch (JSONException | NullPointerException e) {
            log.error("JSON error", e);
            return authentication;
        }

        Member member = this.memberRepository.findById(username)
                .orElse(new Member("NON_MEMBER", Role.ROLE_NON_MEMBER));
        log.trace("requester principal: {}", member);
        SingleAuthorityAuthentication result = new SingleAuthorityAuthentication(
                member.getMemberId(), new SimpleGrantedAuthority(member.getRole().name())
        );
        log.info("result={}", result);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthentication.class.isAssignableFrom(authentication);
    }
}

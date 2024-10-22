package nsl.webmapia.game.auth.authentication;

import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@ToString
public class SingleAuthorityAuthentication implements Authentication {
    private final String username;
    private final Collection<GrantedAuthority> grantedAuthority;

    public SingleAuthorityAuthentication(String username, GrantedAuthority grantedAuthority) {
        this.username = username;
        this.grantedAuthority = Set.of(grantedAuthority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthority;
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDetails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}

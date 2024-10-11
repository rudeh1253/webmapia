package nsl.webmapia.authserver.domain.member.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String roleName;

    public String roleName() {
        return this.roleName;
    }
}

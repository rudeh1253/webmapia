package nsl.webmapia.game.domain.member.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ROLE_MEMBER("MEMBER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_NON_MEMBER("NON_MEMBER");

    private final String role;

    public String roleName() {
        return this.role;
    }
}

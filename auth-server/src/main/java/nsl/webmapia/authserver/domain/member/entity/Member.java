package nsl.webmapia.authserver.domain.member.entity;

import lombok.*;
import nsl.webmapia.authserver.domain.member.constant.Role;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Member {
    private String memberId;
    private String password;
    private Role role = Role.ROLE_USER;
    private String nickname;
    private LocalDateTime creationTime = LocalDateTime.now();
}

package nsl.webmapia.game.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nsl.webmapia.game.member.constant.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    private String memberId;

    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_MEMBER;

    private LocalDateTime creationTime = LocalDateTime.now();

    public Member(String memberId) {
        this.memberId = memberId;
    }

    public Member(String memberId, String nickname) {
        this(memberId);
        this.nickname = nickname;
    }

    public Member(String memberId, String password, String nickname) {
        this(memberId, nickname);
        this.password = password;
    }
}

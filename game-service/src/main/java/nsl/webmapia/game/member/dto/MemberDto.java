package nsl.webmapia.game.member.dto;

import lombok.*;
import nsl.webmapia.game.member.domain.Member;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class MemberDto {
    private String memberId;
    private String nickname;

    public static MemberDto of(Member member) {
        return new MemberDto(member.getMemberId(), member.getNickname());
    }
}

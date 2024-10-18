package nsl.webmapia.game.member.dto;

import lombok.*;
import nsl.webmapia.game.member.entity.Member;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MemberDto {
    private String memberId;
    private String nickname;
    private LocalDateTime creationTime;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .creationTime(member.getCreationTime())
                .build();
    }
}

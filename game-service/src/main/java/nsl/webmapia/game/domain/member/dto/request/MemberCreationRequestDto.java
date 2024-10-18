package nsl.webmapia.game.domain.member.dto.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MemberCreationRequestDto {
    private String memberId;
    private String password;
    private String nickname;
}

package nsl.webmapia.authserver.domain.member.repository.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MemberUpdateDto {
    private String nickname;
}

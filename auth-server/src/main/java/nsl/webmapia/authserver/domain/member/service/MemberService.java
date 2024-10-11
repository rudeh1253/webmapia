package nsl.webmapia.authserver.domain.member.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.authserver.domain.member.entity.Member;
import nsl.webmapia.authserver.domain.member.exception.MemberNotFoundException;
import nsl.webmapia.authserver.domain.member.repository.MemberRepository;
import nsl.webmapia.authserver.domain.member.repository.dto.MemberUpdateDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void createMember(Member member) {
        this.memberRepository.save(member);
    }

    public Member getMember(final String memberId) {
        return this.memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    public void updateMemberNickname(String memberId, String newNickname) {
        MemberUpdateDto updateDto = MemberUpdateDto.builder()
                .nickname(newNickname)
                .build();
        this.memberRepository.updateById(memberId, updateDto);
    }

    public void deleteMember(String memberId) {
        this.memberRepository.deleteById(memberId);
    }
}

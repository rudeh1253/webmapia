package nsl.webmapia.game.domain.member.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.member.dto.MemberDto;
import nsl.webmapia.game.domain.member.dto.request.MemberCreationRequestDto;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void createMember(MemberCreationRequestDto dto) {
        Member member = new Member(dto.getMemberId(), dto.getPassword(), dto.getNickname());
        this.memberRepository.save(member);
    }

    public MemberDto getMember(String memberId) {
        return MemberDto.of(this.memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new));
    }
}

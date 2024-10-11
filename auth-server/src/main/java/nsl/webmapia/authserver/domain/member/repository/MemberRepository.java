package nsl.webmapia.authserver.domain.member.repository;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.authserver.domain.member.entity.Member;
import nsl.webmapia.authserver.domain.member.repository.dto.MemberUpdateDto;
import nsl.webmapia.authserver.domain.member.repository.mybatis.MybatisMemberMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MybatisMemberMapper memberMapper;

    public void save(Member member) {
        this.memberMapper.save(member);
    }

    public Optional<Member> findById(String memberId) {
        return this.memberMapper.findById(memberId);
    }

    public void updateById(String memberId, MemberUpdateDto updateDto) {
        this.memberMapper.updateById(memberId, updateDto);
    }

    public void deleteById(String memberId) {
        this.memberMapper.deleteById(memberId);
    }
}

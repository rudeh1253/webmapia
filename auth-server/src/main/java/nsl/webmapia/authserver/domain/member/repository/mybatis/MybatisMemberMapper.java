package nsl.webmapia.authserver.domain.member.repository.mybatis;

import nsl.webmapia.authserver.domain.member.entity.Member;
import nsl.webmapia.authserver.domain.member.repository.dto.MemberUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MybatisMemberMapper {

    void save(Member member);

    Optional<Member> findById(String memberId);

    void updateById(@Param("memberId") String memberId,
                    @Param("updateDto") MemberUpdateDto updateDto);

    void deleteById(String memberId);

    void clear();
}

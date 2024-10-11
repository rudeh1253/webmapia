package nsl.webmapia.authserver.domain.member.repository;

import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.authserver.domain.member.entity.Member;
import nsl.webmapia.authserver.domain.member.repository.mybatis.MybatisMemberMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class TestMemberRepository {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MybatisMemberMapper memberMapper;

    @AfterEach
    void afterEach() {
        this.memberMapper.clear();
    }

    @DisplayName("save - success")
    @Test
    void save_success() {
        Member memberToInsert = Member.builder()
                .memberId("sample")
                .password("asdf")
                .nickname("nick")
                .build();
        assertThatNoException()
                .isThrownBy(() -> this.memberRepository.save(memberToInsert));
    }

    @DisplayName("save - success - password isn't required field")
    @Test
    void save_success_passwordNotRequired() {
        Member memberToInsert = Member.builder()
                .memberId("sample")
                .nickname("nick")
                .build();
        assertThatNoException()
                .isThrownBy(() -> this.memberRepository.save(memberToInsert));
    }

    static Stream<Member> save_fail_DataIntegrityViolationException() {
        return Stream.of(
                Member.builder()
                        .memberId("sample-nickname")
                        .build(),
                Member.builder()
                        .nickname("sample-nickname")
                        .build(),
                Member.builder().build()
        );
    }

    @DisplayName("save - fail - DataIntegrityViolationException")
    @MethodSource
    @ParameterizedTest
    void save_fail_DataIntegrityViolationException(Member member) {
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> this.memberRepository.save(member));
    }

    private Member insertSampleMember() {
        Member sampleMember = Member.builder()
                .memberId("sample1")
                .password("1234")
                .nickname("nick")
                .build();
        this.memberRepository.save(sampleMember);
        return sampleMember;
    }

    @DisplayName("findById - success")
    @Test
    void findById_success() {
        Member sampleMember = insertSampleMember();
        Optional<Member> memberOp = this.memberRepository.findById(sampleMember.getMemberId());
        assertThat(memberOp).isNotEmpty();
        assertThat(memberOp.get().getMemberId()).isEqualTo(sampleMember.getMemberId());
    }

    @DisplayName("findById - no such member")
    @Test
    void findById_noSuchMember() {
        Member sampleMember = insertSampleMember();
        Optional<Member> memberOp = this.memberRepository.findById(sampleMember.getMemberId() + "suffix");
        assertThat(memberOp).isEmpty();
    }
}
package nsl.webmapia.game.domain.member.repository;

import nsl.webmapia.game.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}

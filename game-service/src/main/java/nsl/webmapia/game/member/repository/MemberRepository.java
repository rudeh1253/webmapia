package nsl.webmapia.game.member.repository;

import nsl.webmapia.game.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}

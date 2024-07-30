package nsl.webmapia.game.member.service;

import nsl.webmapia.game.member.dto.MemberDto;

public interface MemberService {

    MemberDto addMember(MemberDto memberDto);

    MemberDto findMemberById(String memberId);
}

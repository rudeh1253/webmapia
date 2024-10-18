package nsl.webmapia.game.domain.member.controller;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.member.dto.MemberDto;
import nsl.webmapia.game.domain.member.dto.request.MemberCreationRequestDto;
import nsl.webmapia.game.domain.member.service.MemberService;
import nsl.webmapia.game.global.rest.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createMember(@RequestBody MemberCreationRequestDto dto) {
        this.memberService.createMember(dto);
        return new ResponseEntity<>(
                BaseResponse.created("Member has been created"),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<BaseResponse<MemberDto>> findMember(@PathVariable("memberId") String memberId) {
        return new ResponseEntity<>(
                BaseResponse.ok(this.memberService.getMember(memberId)),
                HttpStatus.OK
        );
    }
}

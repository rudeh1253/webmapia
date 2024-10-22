package nsl.webmapia.game.auth.controller;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.member.dto.request.MemberCreationRequestDto;
import nsl.webmapia.game.domain.member.service.MemberService;
import nsl.webmapia.game.global.rest.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<BaseResponse<Void>> createMember(@RequestBody MemberCreationRequestDto dto) {
        this.memberService.createMember(dto);
        return new ResponseEntity<>(
                BaseResponse.created("Member has been created"),
                HttpStatus.CREATED
        );
    }
}

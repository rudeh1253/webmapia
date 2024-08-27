package nsl.webmapia.game.skill.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.common.rest.BaseResponse;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.domain.editor.SkillTypeEditor;
import nsl.webmapia.game.skill.dto.request.SkillActivationRequestDto;
import nsl.webmapia.game.skill.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SkillRestController {
    private final SkillService skillService;

    @InitBinder
    public void bind(WebDataBinder binder) {
        binder.registerCustomEditor(SkillType.class, SkillTypeEditor.getInstance());
    }

    @GetMapping("/game-instances/{gameInstanceId}/available-skills/{memberId}")
    @Operation(summary = "Get available skill types", description = "Get skills a member available")
    @ApiResponses({
            @ApiResponse(description = "Success", responseCode = "200")
    })
    public ResponseEntity<BaseResponse<Map<SkillType, List<String>>>> getAvailableSkillsOfMember(
            @PathVariable int gameInstanceId,
            @PathVariable String memberId
    ) {
        return ResponseEntity.ok(BaseResponse.ok(this.skillService.getAvailableSkills(gameInstanceId, memberId)));
    }

    @PostMapping("/game-instance/{gameInstanceId}/skills/activate")
    @Operation(summary = "Activate skill", description = "Activate a type of skill of a member")
    @ApiResponses({
            @ApiResponse(description = "Successfully activated", responseCode = "200"),
            @ApiResponse(description = "No such game instance", responseCode = "404")
    })
    public ResponseEntity<BaseResponse<Void>> activateSkill(@PathVariable int gameInstanceId,
                                                            @RequestBody SkillActivationRequestDto requestDto) {
        this.skillService.activateSkill(gameInstanceId,
                requestDto.getActivatorId(),
                requestDto.getTargetId(),
                requestDto.getSkillType());
        return ResponseEntity.ok(BaseResponse.ok("Skill activation succeeded"));
    }
}

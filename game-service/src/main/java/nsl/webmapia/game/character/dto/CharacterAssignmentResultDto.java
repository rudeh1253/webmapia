package nsl.webmapia.game.character.dto;

import lombok.Builder;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.member.dto.MemberDto;

@Builder
public record CharacterAssignmentResultDto(
        Integer assignmentId,
        MemberDto assignedMember,
        CharacterCode characterCode
) {

    public static CharacterAssignmentResultDto of(CharacterAssignment characterAssignment) {
        return new CharacterAssignmentResultDto(
                characterAssignment.getAssignmentId(),
                MemberDto.of(characterAssignment.getMember()),
                characterAssignment.getCharacterCode()
        );
    }
}

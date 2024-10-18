package nsl.webmapia.game.domain.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nsl.webmapia.game.domain.vote.dto.VoteDto;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class VoteResultResponseDto {
    private Integer characterAssignmentToBeExecuted;
    private List<VoteDto> votes;
}

package nsl.webmapia.game.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nsl.webmapia.game.vote.dto.VoteDto;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class VoteResultResponseDto {
    private String memberIdToBeExecuted;
    private List<VoteDto> votes;
}

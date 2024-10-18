package nsl.webmapia.game.vote.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class VoteRequestDto {
    private int gameInstanceId;
    private Integer voterId;
    private Integer targetId;
}

package nsl.webmapia.game.gameoperation.service;

import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;

import java.util.List;

/**
 * Object of service layer of game operation.
 * This object manages operation of instances of games.
 *
 * @author PGD
 */
public interface GameService {

    /**
     * Given roomId, start game and create a game instance.
     *
     * @param roomId to start game
     * @return gameInstanceId auto-generated
     */
    Integer startGame(int roomId);

    /**
     * Process a vote from a single member. The size of a single vote is determined by the character of
     * the member owns.
     *
     * @param voteRequestDto DTO contains data of voter id and target id
     * @return a list of vote executed in the current instance
     */
    List<VoteDto> vote(VoteRequestDto voteRequestDto);

    /**
     * Request to end the phase. When all members request to end the phase, the phase ends.
     *
     * @param gameInstanceId of the GameInstance
     * @param requesterId of Member who requested to end the phase
     * @return PhaseResultResponseDto that contains information of the result of the phase
     */
    PhaseResultResponseDto endPhase(int gameInstanceId, String requesterId);
}

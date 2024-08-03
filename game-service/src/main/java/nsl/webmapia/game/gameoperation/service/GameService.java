package nsl.webmapia.game.gameoperation.service;

import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
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
     * @return BaseSystemMessageResponseDto object containing a system message
     * to notify start of game to participants of the game room
     */
    BaseSystemMessageResponseDto<Void> startGame(int roomId);

    /**
     * Process a vote from a single member. The size of a single vote is determined by the character of
     * the member owns.
     *
     * @param voteRequestDto DTO contains data of voter id and target id
     * @return object containing state of vote of current instance
     */
    BaseSystemMessageResponseDto<List<VoteDto>> vote(VoteRequestDto voteRequestDto);

    /**
     * Request to end the phase. When all members request to end the phase, the phase ends.
     *
     * @param roomId of GameRoom the GameInstance belongs to
     * @param requesterId of Member who requested to end the phase
     * @return PhaseResultResponseDto that contains information of the result of the phase
     */
    BaseSystemMessageResponseDto<PhaseResultResponseDto> endPhase(int roomId, String requesterId);
}

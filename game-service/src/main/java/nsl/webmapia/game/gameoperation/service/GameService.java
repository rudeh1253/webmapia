package nsl.webmapia.game.gameoperation.service;

import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.gameoperation.domain.Vote;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;

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
     * @param voteDto DTO contains data of voter id and target id
     * @return object containing state of vote of current instance
     */
    BaseSystemMessageResponseDto<List<Vote>> vote(VoteRequestDto voteDto);

    BaseSystemMessageResponseDto<>
}

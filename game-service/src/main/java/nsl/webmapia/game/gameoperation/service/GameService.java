package nsl.webmapia.game.gameoperation.service;

import nsl.webmapia.game.gameoperation.dto.VoteDto;
import nsl.webmapia.game.gameoperation.dto.request.CharacterDistributionRequestDto;
import nsl.webmapia.game.gameoperation.dto.request.VoteRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.CharacterDistributionResponseDto;
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
     * For each participant of the game instance, assign a character to the participant.
     * If sum of CharacterDistributionsRequestDto.numByCharacters.values() is less than the number of
     * participants, then the rest will be assigned to CITIZEN.
     *
     * @param dto request DTO containing information about for each character how many members will be assigned to that
     * @return a DTO represents distribution of characters
     * @throws IllegalArgumentException if CharacterDistributionsRequestDto.numByCharacters.values() exceeds the number
     *                                  of members which participate in the game instance
     */
    CharacterDistributionResponseDto distributeCharacters(CharacterDistributionRequestDto dto)
            throws IllegalArgumentException;

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
     * @param requesterId    of Member who requested to end the phase
     * @return PhaseResultResponseDto that contains information of the result of the phase
     */
    PhaseResultResponseDto endPhase(int gameInstanceId, String requesterId);
}

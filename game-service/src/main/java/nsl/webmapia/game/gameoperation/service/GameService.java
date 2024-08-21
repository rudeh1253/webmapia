package nsl.webmapia.game.gameoperation.service;

import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.dto.GameInstanceDto;
import nsl.webmapia.game.gameoperation.dto.request.CharacterDistributionRequestDto;
import nsl.webmapia.game.gameoperation.dto.response.CharacterDistributionResponseDto;
import nsl.webmapia.game.gameoperation.dto.response.GameResultResponseDto;

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

    GameInstanceDto getGameInstance(int gameInstanceId);

    GamePhase proceedPhase(int gameInstanceId);

    GameResultResponseDto processGameResult(int gameInstanceId);
}

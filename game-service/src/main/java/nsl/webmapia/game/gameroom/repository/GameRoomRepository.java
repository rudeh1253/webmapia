package nsl.webmapia.game.gameroom.repository;

import nsl.webmapia.game.gameroom.domain.GameRoom;

import java.util.Optional;

/**
 * Repository interface for dealing with game room data
 */
public interface GameRoomRepository {

    /**
     * Save an instance of GameRoom into repository. Whether roomId is null or not,
     * the roomId will be set automatically.
     *
     * @param gameRoom representing information about GameRoom to store. roomId may or may not be null
     * @return generated roomId
     */
    int save(GameRoom gameRoom);

    /**
     * Find GameRoom instance with the given roomId. The instance of the parameter may not exist.
     *
     * @param roomId of GameRoom instance to find
     * @return an instance of Optional object containing either GameRoom instance or null
     */
    Optional<GameRoom> findById(int roomId);

    /**
     * Update GameRoom instance in repository. The information is
     * contained in DTO instance.
     *
     * @param dto an instance containing information how to modify GameRoom
     * @return true if the GameRoom instance with roomId of DTO instance
     * existed such that it has been updated successfully, otherwise false
     */
    boolean update(GameRoomUpdateDto dto);

    /**
     * Delete GameRoom instance from repository.
     *
     * @param roomId of the instance to delete from
     * @return true if the GameRoom instance with roomId existed such that
     * it has been deleted successfully, otherwise false
     */
    boolean deleteById(int roomId);
}

package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.entity.GameInstance;

import java.util.Optional;

public interface GameInstanceRepository {

    /**
     * Save the GameInstance instance into the GameInstanceRepository.
     * The identifier is equal to that of GameRoom.
     *
     * @param gameInstance to be saved into the repository
     */
    void save(GameInstance gameInstance);

    /**
     * Find GameInstance instance given the identifier.
     *
     * @param id of GameInstance
     * @return a GameInstance instance whose identifier is the same as id wrapped with java.util.Optional
     */
    Optional<GameInstance> findById(int id);

    /**
     * Find GameInstance instance given gameRoomId. The GameInstance returned hasn't been
     * terminated. i.e. endTime hasn't been set (means, null).
     *
     * @param gameRoomId of the gameInstance belongs to
     * @return GameInstance object whose endTime is null
     * @throws IllegalStateException when there are more than 1 GameInstance instances each of which has null endTime.
     */
    Optional<GameInstance> findAliveGameInstanceByGameRoomId(int gameRoomId) throws IllegalStateException;

    /**
     * Update given gameRoomId.
     *
     * @param dto containing data to be. Fields of null are ignored
     * @return true if GameInstance of gameRoomId exists such that it succeeded to
     * update GameInstance, otherwise false
     */
    boolean updateByGameRoomId(GameInstanceUpdateDto dto);
}

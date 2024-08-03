package nsl.webmapia.game.gameoperation.repository;

import nsl.webmapia.game.gameoperation.domain.GameInstance;

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
     * Find GameInstance instance given the identifier. The identifier
     * is the same as that of GameRoom associated to the GameInstance.
     *
     * @param id of GameInstance
     * @return a GameInstance instance whose identifier is the same as id wrapped with java.util.Optional
     */
    Optional<GameInstance> findById(int id);
}

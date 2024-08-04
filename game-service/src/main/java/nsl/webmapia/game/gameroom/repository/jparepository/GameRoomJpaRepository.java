package nsl.webmapia.game.gameroom.repository.jparepository;

import nsl.webmapia.game.gameroom.domain.GameRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GameRoomJpaRepository extends JpaRepository<GameRoom, Integer> {

    Page<GameRoom> findByRoomNameLike(@Param("roomName") String roomName, Pageable pageable);
}

package nsl.webmapia.game.gameroom.repository;

import nsl.webmapia.game.gameroom.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

    @Query("""
            SELECT p FROM Participation p
            WHERE p.gameRoom.roomId = :gameRoomId
                AND p.disconnected = FALSE
            """)
    List<Participation> findNotDisconnectedByGameRoomId(int gameRoomId);

    @Query("""
            SELECT p.participationId, p.participantId
            FROM Participation p
            INNER JOIN GameInstance g ON p.gameRoom.roomId =  g.gameRoom.roomId
            WHERE g.gameInstanceId = :gameInstanceId
            """)
    List<Participation> findNotDisconnectedByGameInstanceId(int gameInstanceId);
}

package nsl.webmapia.game.gameroom.repository;

import nsl.webmapia.game.gameroom.domain.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
}

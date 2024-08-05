package nsl.webmapia.game.gameroom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "participation")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
    private Integer participationId;

    @Column(name = "participant_id")
    private String participantId;

    @Column(name = "disconnected")
    private boolean disconnected;

    @JoinColumn(name = "game_room_id")
    @ManyToOne
    private GameRoom gameRoom;

    public Participation(String participantId, GameRoom gameRoom) {
        this.participantId = participantId;
        this.gameRoom = gameRoom;
        this.gameRoom.getParticipationList().add(this);
    }
}

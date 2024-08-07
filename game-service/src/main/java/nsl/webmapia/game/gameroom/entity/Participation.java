package nsl.webmapia.game.gameroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "participation")
@NoArgsConstructor
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
    private boolean disconnected = false;

    @JoinColumn(name = "game_room_id")
    @ManyToOne
    private GameRoom gameRoom;

    public Participation(String participantId, GameRoom gameRoom) {
        this.participantId = participantId;
        this.gameRoom = gameRoom;
        this.gameRoom.getParticipationList().add(this);
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }
}

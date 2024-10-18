package nsl.webmapia.game.gameroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nsl.webmapia.game.member.entity.Member;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Member participant;

    @Column(name = "host")
    private boolean host = false;

    @Column(name = "disconnected")
    private boolean disconnected = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_room_id")
    private GameRoom gameRoom;

    public Participation(Member participant, boolean host, GameRoom gameRoom) {
        this(participant, gameRoom);
        this.host = host;
    }

    public Participation(Member participant, GameRoom gameRoom) {
        this.participant = participant;
        this.gameRoom = gameRoom;
        this.gameRoom.getParticipationList().add(this);
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setHost(boolean host) {
        this.host = host;
    }
}

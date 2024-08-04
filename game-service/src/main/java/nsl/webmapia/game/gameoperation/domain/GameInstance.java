package nsl.webmapia.game.gameoperation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nsl.webmapia.game.gameroom.domain.GameRoom;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_instance")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_instance_id")
    private Integer gameInstanceId;

    @Column(name = "round")
    private int round;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_phase")
    private GamePhase gamePhase;

    @JoinColumn(name = "room_id")
    @OneToOne
    private GameRoom gameRoom;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameInstance toCompare) {
            return this.gameRoom.getRoomId().equals(toCompare.gameRoom.getRoomId())
                    && this.startTime.equals(toCompare.startTime);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.gameRoom.getRoomId().hashCode() * this.startTime.hashCode();
    }
}

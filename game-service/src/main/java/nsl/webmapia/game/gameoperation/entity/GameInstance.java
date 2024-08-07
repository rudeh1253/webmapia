package nsl.webmapia.game.gameoperation.entity;

import jakarta.persistence.*;
import lombok.*;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameroom.entity.GameRoom;

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
    @Setter(AccessLevel.NONE)
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

    @Builder(access = AccessLevel.PUBLIC)
    private GameInstance(int round,
                         LocalDateTime startTime,
                         LocalDateTime endTime,
                         GamePhase gamePhase,
                         GameRoom gameRoom) {
        this.round = round;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gamePhase = gamePhase;
        this.gameRoom = gameRoom;
    }

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

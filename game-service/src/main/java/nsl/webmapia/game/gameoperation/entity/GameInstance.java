package nsl.webmapia.game.gameoperation.entity;

import jakarta.persistence.*;
import lombok.*;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameroom.entity.GameRoom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game_instance")
@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "phase_end_time")
    private LocalDateTime phaseEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_phase")
    private GamePhase gamePhase;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private GameRoom gameRoom;

    @OneToMany(mappedBy = "gameInstance", fetch = FetchType.LAZY)
    private List<CharacterAssignment> characterAssignments = new ArrayList<>();

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

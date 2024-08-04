package nsl.webmapia.game.gameroom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game_room")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "host_member_id")
    private String hostMemberId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @OneToMany(mappedBy = "gameRoom")
    private List<Participation> participationList;

    public GameRoom(String roomName,
                    String hostMemberId,
                    LocalDateTime creationTime) {
        this.roomName = roomName;
        this.hostMemberId = hostMemberId;
        this.creationTime = creationTime;
    }
}

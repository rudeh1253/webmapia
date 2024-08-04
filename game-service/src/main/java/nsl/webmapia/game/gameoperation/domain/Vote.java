package nsl.webmapia.game.gameoperation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class representing an instance of vote.
 */
@Entity
@Table(name = "vote")
@NoArgsConstructor
@Getter
@ToString
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Integer voteId;

    @Column(name = "round")
    private int round;

    @Column(name = "voter_id")
    private String voterId;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "vote_count")
    private int voteCount;

    @ManyToOne
    @JoinColumn(name = "game_instance_id")
    private GameInstance gameInstance;

    public Vote(int round, String voterId, String targetId, int voteCount, GameInstance gameInstance) {
        this.round = round;
        this.voterId = voterId;
        this.targetId = targetId;
        this.voteCount = voteCount;
        this.gameInstance = gameInstance;
    }
}

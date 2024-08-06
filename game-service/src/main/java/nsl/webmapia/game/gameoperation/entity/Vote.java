package nsl.webmapia.game.gameoperation.entity;

import jakarta.persistence.*;
import lombok.*;
import nsl.webmapia.game.gameoperation.entity.GameInstance;

import java.io.Serializable;

/**
 * Class representing an instance of vote.
 */
@Entity
@Table(name = "vote")
@NoArgsConstructor
@Getter
@ToString
public class Vote {

    @EmbeddedId
    private VoteId voteId;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "vote_count")
    private int voteCount;

    @MapsId("gameInstanceId")
    @ManyToOne
    @JoinColumn(name = "game_instance_id")
    private GameInstance gameInstance;

    public Vote(int round,
                String voterId,
                String targetId,
                int voteCount,
                GameInstance gameInstance) {
        this.voteId = new VoteId(round, voterId, gameInstance.getGameInstanceId());
        this.targetId = targetId;
        this.voteCount = voteCount;
        this.gameInstance = gameInstance;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @ToString
    public static class VoteId implements Serializable {

        @Column(name = "round")
        private int round;

        @Column(name = "voter_id")
        private String voterId;

        private int gameInstanceId;
    }
}

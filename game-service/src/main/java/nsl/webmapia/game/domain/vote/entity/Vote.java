package nsl.webmapia.game.domain.vote.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;

import java.io.Serializable;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_character_assignment_id")
    private CharacterAssignment target;

    @Column(name = "vote_count")
    private int voteCount;

    @MapsId("gameInstanceId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_instance_id")
    private GameInstance gameInstance;

    public Vote(int round,
                CharacterAssignment voter,
                CharacterAssignment target,
                int voteCount,
                GameInstance gameInstance) {
        this.voteId = new VoteId(round, voter, gameInstance.getGameInstanceId());
        this.target = target;
        this.voteCount = voteCount;
        this.gameInstance = gameInstance;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    public static class VoteId implements Serializable {

        @Column(name = "round")
        private int round;

        @ManyToOne
        @JoinColumn(name = "voter_character_assignment_id")
        private CharacterAssignment voter;

        private int gameInstanceId;

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Vote target) {
                return this.round == target.voteId.round
                        && this.voter.getAssignmentId().equals(target.voteId.voter.getAssignmentId())
                        && this.gameInstanceId == target.voteId.gameInstanceId;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.round, this.voter.getAssignmentId(), this.gameInstanceId);
        }
    }
}

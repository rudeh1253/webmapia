package nsl.webmapia.game.character.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.gameoperation.entity.GameInstance;

@Entity
@Table(name = "character_assignment")
@NoArgsConstructor
@Getter
@Setter
public class CharacterAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Integer assignmentId;

    @Column(name = "member_id")
    private String memberId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "character_code")
    private CharacterCode characterCode;

    @Column(name = "life")
    private int life;

    @ManyToOne
    @JoinColumn(name = "game_instance_id")
    private GameInstance gameInstance;

    public boolean isDead() {
        return this.life < 1;
    }
}

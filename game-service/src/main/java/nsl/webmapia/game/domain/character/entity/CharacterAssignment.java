package nsl.webmapia.game.domain.character.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.member.entity.Member;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "character_code")
    private CharacterCode characterCode;

    @Column(name = "life")
    private int life = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_instance_id")
    private GameInstance gameInstance;

    public CharacterAssignment(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public CharacterAssignment(Member member, CharacterCode characterCode, GameInstance gameInstance) {
        this.member = member;
        this.characterCode = characterCode;
        this.gameInstance = gameInstance;
        gameInstance.getCharacterAssignments().add(this);
    }

    public boolean isDead() {
        return this.life < 1;
    }
}

package nsl.webmapia.game.skill.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.skill.domain.SkillType;

@Entity
@Table(name = "activated_skill")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ActivatedSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activated_skill_id")
    private Integer activatedSkillId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "skill_type")
    private SkillType skillType;

    @Column(name = "round")
    private int round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activator_id")
    private CharacterAssignment activator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private CharacterAssignment target;
}

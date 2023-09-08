import strResource from "../../../../resource/string.json";
import skillsPerChars from "../../../game/skillsPerChars";
import {useAppSelector} from "../../../redux/hook";
import {SkillType} from "../../../type/gameDomainType";

export default function NightPhase() {
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const skills = skillsPerChars.get(thisUser.characterCode);
    return (
        <div className="game-container night-phase">
            <p>{strResource.game.night}</p>
            {skills?.map((e, idx) => {
                return (
                    <SkillSelection
                        skillName={e.skillName}
                        skillDesc={e.skillDesc}
                        skillType={e.skillType}
                    />
                );
            })}
        </div>
    );
}

type SkillSelectionProps = {
    skillName: string;
    skillDesc: string;
    skillType: SkillType;
};

function SkillSelection({
    skillName,
    skillDesc,
    skillType
}: SkillSelectionProps) {
    return (
        <div className="selection-container">
            <button className="selection-btn" type="button">
                {skillName}
            </button>
            <p className="skill-desc">{skillDesc}</p>
        </div>
    );
}

import strResource from "../../resource/string.json";
import {CharacterCode, SkillType} from "../type/gameDomainType";

const skillsPerChars = new Map<
    CharacterCode,
    {skillName: string; skillDesc: string; skillType: SkillType}[]
>([
    [
        "WOLF",
        [
            {
                skillName: strResource.skill.kill,
                skillDesc: "",
                skillType: "KILL"
            },
            {
                skillName: strResource.skill.exterminate,
                skillDesc: "",
                skillType: "EXTERMINATE"
            }
        ]
    ],
    [
        "BETRAYER",
        [
            {
                skillName: strResource.skill.investigationAlive,
                skillDesc: "",
                skillType: "INVESTIGATE_ALIVE_CHARACTER"
            }
        ]
    ],
    [
        "FOLLOWER",
        [
            {
                skillName: strResource.skill.insight,
                skillDesc: "",
                skillType: "INVESTIGATE_ALIVE_CHARACTER"
            }
        ]
    ],
    [
        "PREDICTOR",
        [
            {
                skillName: strResource.skill.prediction,
                skillDesc: "",
                skillType: "INVESTIGATE_ALIVE_CHARACTER"
            }
        ]
    ],
    [
        "GUARD",
        [
            {
                skillName: strResource.skill.guard,
                skillDesc: "",
                skillType: "GUARD"
            }
        ]
    ],
    [
        "MEDIUMSHIP",
        [
            {
                skillName: strResource.skill.investigationDead,
                skillDesc: "",
                skillType: "INVESTIGATE_DEAD_CHARACTER"
            }
        ]
    ],
    [
        "DETECTIVE",
        [
            {
                skillName: strResource.skill.investigationAlive,
                skillDesc: "",
                skillType: "INVESTIGATE_ALIVE_CHARACTER"
            }
        ]
    ],
    ["SUCCESSOR", []],
    ["SECRET_SOCIETY", []],
    ["NOBILITY", []],
    ["SOLDIER", []],
    ["TEMPLAR", []],
    ["CITIZEN", []],
    [
        "MURDERER",
        [
            {
                skillName: strResource.skill.murder,
                skillDesc: "",
                skillType: "EXTERMINATE"
            }
        ]
    ],
    ["HUMAN_MOUSE", []]
]);

export default skillsPerChars;
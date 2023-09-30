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
                skillDesc: strResource.skill.skillDescription.wolf.kill,
                skillType: "KILL"
            },
            {
                skillName: strResource.skill.exterminate,
                skillDesc: strResource.skill.skillDescription.wolf.extermination,
                skillType: "EXTERMINATE"
            }
        ]
    ],
    [
        "BETRAYER",
        [
            {
                skillName: strResource.skill.investigationDead,
                skillDesc: strResource.skill.skillDescription.betrayer.investigationDead,
                skillType: "INVESTIGATE_DEAD_CHARACTER"
            },
            {
                skillName: strResource.skill.enterWolfChat,
                skillDesc: strResource.skill.skillDescription.betrayer.wolfChat,
                skillType: "ENTER_WOLF_CHAT"
            }
        ]
    ],
    [
        "FOLLOWER",
        [
            {
                skillName: strResource.skill.insight,
                skillDesc: strResource.skill.skillDescription.follower.investigationAlive,
                skillType: "INVESTIGATE_ALIVE_CHARACTER"
            },
            {
                skillName: strResource.skill.enterWolfChat,
                skillDesc: strResource.skill.skillDescription.follower.wolfChat,
                skillType: "ENTER_WOLF_CHAT"
            }
        ]
    ],
    [
        "PREDICTOR",
        [
            {
                skillName: strResource.skill.prediction,
                skillDesc: strResource.skill.skillDescription.predictor.prediction,
                skillType: "INVESTIGATE_ALIVE_CHARACTER"
            }
        ]
    ],
    [
        "GUARD",
        [
            {
                skillName: strResource.skill.guard,
                skillDesc: strResource.skill.skillDescription.guard.guard,
                skillType: "GUARD"
            }
        ]
    ],
    [
        "MEDIUMSHIP",
        [
            {
                skillName: strResource.skill.investigationDead,
                skillDesc: strResource.skill.skillDescription.mediumship.investigationDead,
                skillType: "INVESTIGATE_DEAD_CHARACTER"
            }
        ]
    ],
    [
        "DETECTIVE",
        [
            {
                skillName: strResource.skill.investigationAlive,
                skillDesc: strResource.skill.skillDescription.detective.investigationAlive,
                skillType: "INVESTIGATE_ALIVE_CHARACTER"
            }
        ]
    ],
    ["SUCCESSOR", []],
    ["SECRET_SOCIETY", []],
    ["NOBILITY", []],
    [
        "SOLDIER",
        [
            {
                skillName: strResource.skill.selfGuard,
                skillDesc: strResource.skill.skillDescription.soldier.guardSelf,
                skillType: "GUARD"
            }
        ]
    ],
    ["TEMPLAR", []],
    ["CITIZEN", []],
    [
        "MURDERER",
        [
            {
                skillName: strResource.skill.murder,
                skillDesc: strResource.skill.skillDescription.murderer.murder,
                skillType: "EXTERMINATE"
            }
        ]
    ],
    ["HUMAN_MOUSE", []]
]);

export default skillsPerChars;

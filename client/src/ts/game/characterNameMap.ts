import strResource from "../../resource/string.json";
import {CharacterCode} from "../type/gameDomainType";

export const characterNameMap = new Map<CharacterCode, string>([
    ["WOLF", strResource.game.wolf],
    ["BETRAYER", strResource.game.betrayer],
    ["FOLLOWER", strResource.game.follower],
    ["PREDICTOR", strResource.game.predictor],
    ["GUARD", strResource.game.guard],
    ["MEDIUMSHIP", strResource.game.mediumship],
    ["DETECTIVE", strResource.game.detective],
    ["SUCCESSOR", strResource.game.successor],
    ["SECRET_SOCIETY", strResource.game.secretSociety],
    ["NOBILITY", strResource.game.nobility],
    ["SOLDIER", strResource.game.soldier],
    ["TEMPLAR", strResource.game.templar],
    ["CITIZEN", strResource.game.citizen],
    ["MURDERER", strResource.game.murderer],
    ["HUMAN_MOUSE", strResource.game.humanMouse],
    ["GOOD_MAN", strResource.game.goodMan]
]);

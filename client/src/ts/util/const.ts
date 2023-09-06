import serverSpec from "../../resource/secret/server-spec.json";
import strResource from "../../resource/string.json";
import {GamePhase} from "../type/gameDomainType";

export const SOCKET_URL = serverSpec.socketUrl;
export const REST_API_URL = serverSpec.restApiUrl;

export const SOCKET_SEND_CHAT_PUBLIC = `/app/chatroom/public-message`;
export const SOCKET_SEND_CHAT_PRIVATE = `/app/chatroom/private-message`;
export const SOCKET_SEND_USER_EXIT = "/app/game/user-exit";
export const SOCKET_SEND_GAME_START = "/app/game/start";
export const SOCKET_SEND_GAME_DISTRIBUTE_CHARACTER =
    "/app/game/distribute-character";
export const SOCKET_SEND_PHASE_END = "/app/game/end-phase";
export const SOCKET_SEND_POST_PHASE = "/app/game/post-phase";

export const SOCKET_SUBSCRIBE_NOTIFICATION_PUBLIC = (roomId: number) =>
    `/notification/public/${roomId}`;
export const SOCKET_SUBSCRIBE_CHATROOM_PUBLIC = (roomId: number) =>
    `/chatroom/${roomId}`;
export const SOCKET_SUBSCRIBE_CHATROOM_PRIVATE = (
    roomId: number,
    userId: number
) => `/chatroom/${roomId}/private/${userId}`;
export const SOCKET_SUBSCRIBE_NOTIFICATION_PRIVATE = (
    roomId: number,
    userId: number
) => `/notification/private/${roomId}/${userId}`;

export const REST_GAME_ROOM = `${REST_API_URL}/game/room`;
export const REST_USER_ID = `${REST_API_URL}/user/id`;
export const REST_GAME_USER = (roomId: number) =>
    `${REST_API_URL}/game/${roomId}/user`;

export const SECOND_IN_MILLIS = 1000;

export const CHARACTER_NAME_MAP = {
    WOLF: strResource.game.wolf,
    BETRAYER: strResource.game.betrayer,
    FOLLOWER: strResource.game.follower,
    PREDICTOR: strResource.game.predictor,
    GUARD: strResource.game.guard,
    MEDIUMSHIP: strResource.game.mediumship,
    DETECTIVE: strResource.game.detective,
    SUCCESSOR: strResource.game.successor,
    SECRET_SOCIETY: strResource.game.secretSociety,
    NOBILITY: strResource.game.nobility,
    SOLDIER: strResource.game.soldier,
    TEMPLAR: strResource.game.templar,
    CITIZEN: strResource.game.citizen,
    MURDERER: strResource.game.murderer,
    HUMAN_MOUSE: strResource.game.humanMouse
};

export const GAME_PHASE_ORDER = [
    GamePhase.CHARACTER_DISTRIBUTION,
    GamePhase.NIGHT,
    GamePhase.DAYTIME,
    GamePhase.VOTE,
    GamePhase.EXECUTION
];

export const DEFAULT_TIME_CONFIGURATION = {
    discussionTimeSeconds: 5,
    voteTimeSeconds: 5,
    nightTimeSeconds: 5
};

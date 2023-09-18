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
export const SOCKET_SEND_PARTICIPATE_CHAT_CONTAINER =
    "/app/chatroom/participate-chat-container";
export const SOCKET_SEND_NEW_PARTICIPANT_IN_CHAT =
    "/app/chatroom/new-participant-in-chat";
export const SOCKET_SEND_REMOVE_CHAT_CONTAINER =
    "/app/chatroom/remove-chat-container";
export const SOCKET_SEND_ACTIVATE_SKILL = "/app/game/activate-skill";
export const SOCKET_SEND_PROCESS_SKILL = "/app/game/process-skill";
export const SOCKET_SEND_VOTE = "/app/game/vote";

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
export const REST_ONE_GAME_USER = (roomId: number, userId: number) =>
    `${REST_API_URL}/game/${roomId}/user/${userId}`;

export const SECOND_IN_MILLIS = 1000;

export const GAME_PHASE_ORDER = [
    GamePhase.CHARACTER_DISTRIBUTION,
    GamePhase.NIGHT,
    GamePhase.DAYTIME,
    GamePhase.VOTE
];

export const DEFAULT_TIME_CONFIGURATION = {
    discussionTimeSeconds: 60,
    voteTimeSeconds: 60,
    nightTimeSeconds: 60
};

export const ID_OF_PUBLIC_CHAT = 1;
export const ID_OF_WOLF_CHAT = 101;
export const ID_OF_SECRET_SOCIETY_CHAT = 201;
export const ID_OF_CHAT_FOR_DEAD = 444;

export const NAME_OF_WOLF_CHAT = strResource.game.wolfChat;
export const NAME_OF_CHAT_FOR_DEAD = strResource.game.deadChat;

export enum SystemMessengerId {
    USER_ENTERED = -1300,
    USER_EXITED,
    CHARACTER_DISTRIBUTION_STARTED,
    NIGHT_STARTED,
    DAYTIME_STARTED,
    VOTE_STARTED,
    EXECUTION_STARTED,
    YOU_WERE_KILLED,
    YOU_WERE_EXTERMINATED,
    SOMEONE_WAS_KILLED,
    SOMEONE_WAS_EXTERMINATED,
    INVESTIGATION_RESULT,
    GUARD_SUCCESS,
    SKILL_FAIL,
    SOMEONE_WAS_EXECUTED,
    NONE_WAS_EXECUTED,
    GAME_STARTED,
    DUMMY
}

export const MESSAGE_SEPEARTION_ID = -471;

export type SystemMessageInfo = {
    className: string;
};

export const systemMessageTypeMap = new Map<
    SystemMessengerId,
    SystemMessageInfo
>([
    [
        SystemMessengerId.USER_ENTERED,
        {className: strResource.classNames.userEnter}
    ],
    [
        SystemMessengerId.USER_EXITED,
        {className: strResource.classNames.userExit}
    ],
    [
        SystemMessengerId.NIGHT_STARTED,
        {className: strResource.classNames.normal}
    ],
    [
        SystemMessengerId.CHARACTER_DISTRIBUTION_STARTED,
        {className: strResource.classNames.normal}
    ],
    [
        SystemMessengerId.DAYTIME_STARTED,
        {className: strResource.classNames.normal}
    ],
    [
        SystemMessengerId.VOTE_STARTED,
        {className: strResource.classNames.normal}
    ],
    [
        SystemMessengerId.EXECUTION_STARTED,
        {className: strResource.classNames.normal}
    ],
    [
        SystemMessengerId.YOU_WERE_KILLED,
        {className: strResource.classNames.killed}
    ],
    [
        SystemMessengerId.YOU_WERE_EXTERMINATED,
        {className: strResource.classNames.killed}
    ],
    [
        SystemMessengerId.SOMEONE_WAS_KILLED,
        {className: strResource.classNames.killed}
    ],
    [
        SystemMessengerId.SOMEONE_WAS_EXTERMINATED,
        {className: strResource.classNames.killed}
    ],
    [
        SystemMessengerId.INVESTIGATION_RESULT,
        {className: strResource.classNames.investigation}
    ],
    [
        SystemMessengerId.GUARD_SUCCESS,
        {className: strResource.classNames.guard}
    ],
    [
        SystemMessengerId.SKILL_FAIL,
        {className: strResource.classNames.skillFail}
    ],
    [
        SystemMessengerId.SOMEONE_WAS_EXECUTED,
        {className: strResource.classNames.killed}
    ],
    [
        SystemMessengerId.NONE_WAS_EXECUTED,
        {className: strResource.classNames.normal}
    ],
    [SystemMessengerId.DUMMY, {className: strResource.classNames.normal}]
]);

export const phaseText = {
    NIGHT: strResource.game.night,
    DAYTIME: strResource.game.daytime,
    VOTE: strResource.game.vote,
    EXECUTION: strResource.game.execution,
    CHARACTER_DISTRIBUTION: strResource.game.characterDistribution,
    GAME_END: strResource.game.gameEnd
};

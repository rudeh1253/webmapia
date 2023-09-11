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
    GamePhase.VOTE,
    GamePhase.EXECUTION
];

export const DEFAULT_TIME_CONFIGURATION = {
    discussionTimeSeconds: 5,
    voteTimeSeconds: 5,
    nightTimeSeconds: 15
};

export const ID_OF_PUBLIC_CHAT = 1;
export const ID_OF_WOLF_CHAT = 101;
export const ID_OF_SECRET_SOCIETY_CHAT = 201;
export const ID_OF_CHAT_FOR_DEAD = 444;

export const NAME_OF_WOLF_CHAT = strResource.game.wolfChat;

// SYSTEM_MESSAGE_ID + MESSAGE_TYPE = senderId of system message
export const SYSTEM_MESSAGE_ID = -41825;
export enum SystemMessageType {
    YOU_WERE_KILLED = -130,
    YOU_WERE_EXTERMINATED,
    SOMEONE_WAS_KILLED,
    SOMEONE_WAS_EXTERMINATED,
    INVESTIGATION_RESULT,
    GUARD_SUCCESS,
    SKILL_FAIL,
    DUMMY
}

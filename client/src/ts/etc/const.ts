import serverSpec from "../../resource/secret/server-spec.json";

export const SOCKET_URL = serverSpec.socketUrl;
export const REST_API_URL = serverSpec.restApiUrl;

export const SOCKET_SEND_CHAT_PUBLIC = `/app/chatroom/public-message`;
export const SOCKET_SEND_CHAT_PRIVATE = `/app/chatroom/private-message`;
export const SOCKET_SEND_USER_EXIT = "/app/game/user-exit";

export const SOCKET_SUBSCRIBE_NOTIFICATION_PUBLIC = (roomId: number) =>
    `/notification/public/${roomId}`;
export const SOCKET_SUBSCRIBE_CHATROOM_PUBLIC = (roomId: number) =>
    `/chatroom/${roomId}`;
export const SOCKET_SUBSCRIBE_CHATROOM_PRIVATE = (
    roomId: number,
    userId: number
) => `/chatroom/${roomId}/private/${userId}`;

export const REST_GAME_ROOM = `${REST_API_URL}/game/room`;
export const REST_USER_ID = `${REST_API_URL}/user/id`;
export const REST_GAME_USER = (roomId: number) =>
    `${REST_API_URL}/game/${roomId}/user`;

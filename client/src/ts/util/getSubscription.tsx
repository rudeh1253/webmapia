import {
    Chat,
    PrivateChatMessage,
    PublicChatMessage,
    UserInfo
} from "../type/gameDomainType";
import { CommonResponse, UserResponse } from "../type/responseType";
import { CurrentRoomInfoInitialState } from "../redux/slice/currentRoomInfoSlice";
import {
    SOCKET_SUBSCRIBE_CHATROOM_PRIVATE,
    SOCKET_SUBSCRIBE_CHATROOM_PUBLIC,
    SOCKET_SUBSCRIBE_NOTIFICATION_PUBLIC
} from "./const";
import { UserState } from "../component/room/Room";

export function getSubscription(
    currentRoomInfo: CurrentRoomInfoInitialState,
    setNewUserState: React.Dispatch<React.SetStateAction<UserState>>,
    thisUser: UserInfo,
    setNewChat: React.Dispatch<React.SetStateAction<Chat>>
) {
    return [
        {
            endpoint: `${SOCKET_SUBSCRIBE_NOTIFICATION_PUBLIC(
                currentRoomInfo.roomInfo.roomId
            )}`,
            callback: (payload: any) => {
                const payloadData = JSON.parse(payload.body)
                    .body as CommonResponse<UserResponse>;
                switch (payloadData.data.notificationType) {
                    case "USER_ENTERED":
                    case "USER_REMOVED":
                        const userInfo: UserInfo = {
                            userId: payloadData.data.userId,
                            username: payloadData.data.username,
                            characterCode: null,
                            isDead: false
                        };
                        setNewUserState({
                            stateType: payloadData.data.notificationType ===
                                "USER_ENTERED"
                                ? "USER_ENTERED"
                                : payloadData.data.notificationType ===
                                    "USER_REMOVED"
                                    ? "USER_EXITED"
                                    : null,
                            userInfo
                        });
                        break;
                    case "GAME_START":
                        // const body: GameStartNotificationRequest = {
                        //     notificationType: "GAME_START",
                        //     gameId: currentRoomInfo.roomInfo.roomId,
                        // };
                        // sockClient.sendMessage(SOCKET_SEND_GAME_START, {});
                        break;
                }
            }
        },
        {
            endpoint: `${SOCKET_SUBSCRIBE_CHATROOM_PUBLIC(
                currentRoomInfo.roomInfo.roomId
            )}`,
            callback: (payload: any) => {
                const payloadData = JSON.parse(payload.body)
                    .body as CommonResponse<PublicChatMessage>;

                const chat: Chat = {
                    senderId: payloadData.data.senderId,
                    message: payloadData.data.message,
                    timestamp: new Date(payloadData.dateTime).getTime(),
                    isPublic: true,
                    isMe: thisUser.userId === payloadData.data.senderId
                };
                setNewChat(chat);
            }
        },
        {
            endpoint: `${SOCKET_SUBSCRIBE_CHATROOM_PRIVATE(
                currentRoomInfo.roomInfo.roomId,
                thisUser.userId
            )}`,
            callback: (payload: any) => {
                const payloadData = JSON.parse(payload.body)
                    .body as CommonResponse<PrivateChatMessage>;

                const chat: Chat = {
                    senderId: payloadData.data.senderId,
                    message: payloadData.data.message,
                    timestamp: new Date(payloadData.dateTime).getTime(),
                    isPublic: false,
                    isMe: thisUser.userId === payloadData.data.senderId
                };
                setNewChat(chat);
            }
        }
    ];
}

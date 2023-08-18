import {CurrentRoomInfoInitialState} from "../redux/slice/currentRoomInfoSlice";
import SocketClient from "../sockjs/SocketClient";
import {PublicChatMessage, UserInfo} from "../type/gameDomainType";

export const chat = (
    message: string,
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo,
    sockClient: SocketClient
) => {
    const messageObj: PublicChatMessage = {
        gameId: currentRoomInfo.roomInfo.roomId,
        senderId: thisUser.userId,
        message
    };
    sockClient.sendMessage("/app/chatroom/public-message", {}, messageObj);
};

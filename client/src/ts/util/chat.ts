import {CurrentRoomInfoInitialState} from "../redux/slice/currentRoomInfoSlice";
import SocketClient from "../sockjs/SocketClient";
import {PublicChatMessage, UserInfo} from "../type/gameDomainType";
import {
    CreationNewChatContainerRequest,
    NewParticipantRequest,
    RemoveChatContainerRequest
} from "../type/requestType";
import {
    SOCKET_SEND_CHAT_PUBLIC,
    SOCKET_SEND_NEW_CHAT_CONTAINER,
    SOCKET_SEND_NEW_PARTICIPANT_IN_CHAT,
    SOCKET_SEND_REMOVE_CHAT_CONTAINER
} from "./const";

var sockClient: SocketClient;

export async function sendPublicChat(
    message: string,
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo
) {
    const messageObj: PublicChatMessage = {
        gameId: currentRoomInfo.roomInfo.roomId,
        senderId: thisUser.userId,
        message
    };
    if (!sockClient) {
        sockClient = await SocketClient.getInstance();
    }
    sockClient.sendMessage(SOCKET_SEND_CHAT_PUBLIC, {}, messageObj);
}

export async function sendPrivateChat(
    message: string,
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo,
    containerId: number
) {}

export async function createChatContainer(
    gameId: number,
    containerName: string,
    usersToGetIn: number[]
) {
    if (!sockClient) {
        sockClient = await SocketClient.getInstance();
    }
    const body: CreationNewChatContainerRequest = {
        gameId,
        containerName,
        usersToGetIn
    };
    sockClient.sendMessage(SOCKET_SEND_NEW_CHAT_CONTAINER, {}, body);
}

export async function addNewParticipant(
    gameId: number,
    containerId: number,
    userId: number
) {
    if (!sockClient) {
        sockClient = await SocketClient.getInstance();
    }
    const body: NewParticipantRequest = {
        gameId,
        containerId,
        userId
    };
    sockClient.sendMessage(SOCKET_SEND_NEW_PARTICIPANT_IN_CHAT, {}, body);
}

export async function removeChatContainer(gameId: number, containerId: number) {
    if (!sockClient) {
        sockClient = await SocketClient.getInstance();
    }
    const body: RemoveChatContainerRequest = {
        gameId,
        containerId
    };
    sockClient.sendMessage(SOCKET_SEND_REMOVE_CHAT_CONTAINER, {}, body);
}

import {CurrentRoomInfoInitialState} from "../redux/slice/currentRoomInfoSlice";
import {setNewChatContainer} from "../redux/slice/newChatContainerSlice";
import SocketClient from "./SocketClient";
import {
    ChatContainer,
    PrivateChatMessage,
    PublicChatMessage,
    UserInfo
} from "../type/gameDomainType";
import {
    CreationNewChatContainerRequest,
    NewParticipantRequest,
    RemoveChatContainerRequest
} from "../type/requestType";
import {CreationNewChatContainerResponse} from "../type/responseType";
import {
    SOCKET_SEND_CHAT_PRIVATE,
    SOCKET_SEND_CHAT_PUBLIC,
    SOCKET_SEND_NEW_CHAT_CONTAINER,
    SOCKET_SEND_NEW_PARTICIPANT_IN_CHAT,
    SOCKET_SEND_REMOVE_CHAT_CONTAINER
} from "../util/const";

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
    console.log("Send Public Message");
    sockClient.sendMessage(SOCKET_SEND_CHAT_PUBLIC, {}, messageObj);
}

export async function sendPrivateChat(
    message: string,
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo,
    containerId: number
) {
    const messageObj: PrivateChatMessage = {
        gameId: currentRoomInfo.roomInfo.roomId,
        senderId: thisUser.userId,
        message,
        containerId
    };
    if (!sockClient) {
        sockClient = await SocketClient.getInstance();
    }
    sockClient.sendMessage(SOCKET_SEND_CHAT_PRIVATE, {}, messageObj);
}

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

export async function onNewChatContainerCreated(
    chatContainer: CreationNewChatContainerResponse,
    dispatch: any
) {
    const p: UserInfo[] = [];
    for (let i of chatContainer.participants) {
        p.push({
            userId: i,
            username: "",
            characterCode: null,
            isDead: false
        });
    }
    const container: ChatContainer = {
        id: chatContainer.containerId,
        participants: p,
        name: chatContainer.containerName,
        chatLogs: []
    };
    dispatch(setNewChatContainer(container));
}

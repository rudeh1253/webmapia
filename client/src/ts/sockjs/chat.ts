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
    ParticipateChatContainerRequest,
    RemoveChatContainerRequest
} from "../type/requestType";
import {
    ParticipateChatContainerResponse,
    NewParticipantResponse,
    CommonResponse,
    UserResponse
} from "../type/responseType";
import {
    REST_GAME_USER,
    REST_ONE_GAME_USER,
    SOCKET_SEND_CHAT_PRIVATE,
    SOCKET_SEND_CHAT_PUBLIC,
    SOCKET_SEND_PARTICIPATE_CHAT_CONTAINER,
    SOCKET_SEND_REMOVE_CHAT_CONTAINER
} from "../util/const";
import {SystemMessengerId} from "./SystemMessengerId";
import {chatContainerMap} from "../component/room/ChatComponent";
import axios from "axios";

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

export async function sendSystemMessage(
    message: string,
    systemId: SystemMessengerId,
    currentRoomInfo: CurrentRoomInfoInitialState
) {
    const messageObj: PublicChatMessage = {
        gameId: currentRoomInfo.roomInfo.roomId,
        senderId: systemId,
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

export async function participateChatContainer(
    gameId: number,
    containerId: number,
    containerName: string,
    participant: number
) {
    if (!sockClient) {
        sockClient = await SocketClient.getInstance();
    }
    const body: ParticipateChatContainerRequest = {
        gameId,
        containerId,
        containerName,
        participant
    };
    sockClient.sendMessage(SOCKET_SEND_PARTICIPATE_CHAT_CONTAINER, {}, body);
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

export async function onNewParticipantEntered(
    data: ParticipateChatContainerResponse,
    dispatch: any
) {
    const chatContainer = chatContainerMap.get(data.containerId);
    if (chatContainer) {
        const toDispatch = {
            ...chatContainer,
            participants: [...chatContainer.participants, data.newParticipant]
        };
        console.log(toDispatch);
        dispatch(setNewChatContainer(toDispatch));
    } else {
        const userArrCommonResponse = await axios.get<
            CommonResponse<UserResponse[]>
        >(REST_GAME_USER(data.gameId));
        const userArrResponse = userArrCommonResponse.data.data;
        const participantArrResponse = userArrResponse.filter(
            (ur) =>
                data.previousParticipants.includes(ur.userId) ||
                ur.userId === data.newParticipant
        );

        const participants = participantArrResponse.map((resp) => {
            const userInfo: UserInfo = {
                userId: resp.userId,
                username: resp.username,
                characterCode: resp.characterCode,
                isDead: resp.isDead
            };
            return userInfo;
        });

        const participantIds = participants.map((user) => user.userId);

        const toDispatch = {
            id: data.containerId,
            participants: participantIds,
            name: data.containerName,
            chatLogs: []
        };
        console.log(toDispatch);
        dispatch(setNewChatContainer(toDispatch));
    }
}

export async function onNewParticipantInChatContainer(
    data: NewParticipantResponse,
    dispatch: any
) {
    const chatContainer = chatContainerMap.get(data.containerId);
    dispatch(
        setNewChatContainer({
            ...chatContainer!,
            participants: [...chatContainer!.participants, data.newUserId]
        })
    );
}

export async function onEnterChatContainer(
    data: NewParticipantResponse,
    dispatch: any
) {
    const container: ChatContainer = {
        id: data.containerId,
        participants: [...data.userIdsParticipatingAlready, data.newUserId],
        name: data.containerName,
        chatLogs: []
    };
    dispatch(setNewChatContainer(container));
}

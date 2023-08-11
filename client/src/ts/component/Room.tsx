import {useRef, useState, useEffect} from "react";
import {
    Chat,
    PrivateChatMessage,
    PublicChatMessage,
    UserInfo
} from "../type/gameDomainType";
import strResource from "../../resource/string.json";
import serverSpecResource from "../../resource/secret/server-spec.json";
import {useAppDispatch, useAppSelector} from "../redux/hook";
import axios from "axios";
import {CommonResponse, UserResponse} from "../type/responseType";
import SocketClient from "../sockjs/SocketClient";
import {CurrentRoomInfoInitialState} from "../redux/slice/currentRoomInfoSlice";
import {ChatItem, UserItem} from "./HomeSubcomponents";
import {GameConfigurationModal} from "./RoomSubcomponent";
import { setGameConfigurationModal } from "../redux/slice/gameConfigurationModal";

var sockClient: SocketClient;

export default function Room() {
    const [usersInRoom, setUsersInRoom] = useState<UserInfo[]>([]);
    const [newUser, setNewUser] = useState<UserInfo>({
        userId: -1,
        username: "",
        characterCode: null,
        isDead: false
    });
    const [delayStateForNewUser, setDelayStateForNewUser] = useState<UserInfo>({
        userId: -1,
        username: "",
        characterCode: null,
        isDead: false
    });
    const [chatLogs, setChatLogs] = useState<Array<Chat>>([]);
    const [newChat, setNewChat] = useState<Chat>({
        senderId: -1,
        message: "",
        timestamp: -1,
        isPublic: false,
        isMe: false
    });
    const [delayStateForNewChat, setDelayStateForNewChat] = useState<Chat>({
        senderId: -1,
        message: "",
        timestamp: -1,
        isPublic: false,
        isMe: false
    });
    const gameConfigurationModal = useAppSelector(state => state.gameConfigurationModal);

    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const currentRoomInfo = useAppSelector((state) => state.currentRoomInfo);

    const chatInputRef = useRef<HTMLInputElement>(null);

    const dispatch = useAppDispatch();

    useEffect(() => {
        init(
            thisUser,
            currentRoomInfo,
            usersInRoom,
            setUsersInRoom,
            setNewUser,
            setNewChat
        );
        return () => {
            // TODO: send exit message to server
        };
    }, []);

    useEffect(() => {
        if (newUser.userId !== -1) {
            if (delayStateForNewUser.userId !== newUser.userId) {
                setUsersInRoom([...usersInRoom, newUser]);
                setDelayStateForNewUser({...newUser});
            }
        }
    }, [newUser]);

    useEffect(() => {
        if (newChat.senderId !== -1) {
            if (delayStateForNewChat.senderId !== newChat.senderId) {
                setChatLogs([...chatLogs, newChat]);
                setDelayStateForNewChat({...newChat});
            }
        }
    }, [newChat]);

    return (
        <div className="room-container">
            <p>User ID: {thisUser.userId}</p>
            <div className="host-bar">
                {thisUser.userId === currentRoomInfo.roomInfo.hostId ? (
                    <button type="button">{strResource.room.gameStart}</button>
                ) : null}
                <button
                    type="button"
                    onClick={() => dispatch(setGameConfigurationModal(true))}
                >
                    {strResource.room.gameConfiguration}
                </button>
            </div>
            {gameConfigurationModal ? <GameConfigurationModal /> : null}
            <ul className="user-list">
                {usersInRoom.map((user, idx) => (
                    <li key={`user-item-${idx}`}>
                        <UserItem
                            userId={user.userId}
                            username={user.username}
                            characterCode={user.characterCode}
                            isDead={user.isDead}
                        />
                    </li>
                ))}
            </ul>
            <div className="chat-container">
                <div className="chat-log">
                    {chatLogs.map((chat, idx) => (
                        <ChatItem
                            key={`chat-item-${idx + 1}`}
                            senderId={chat.senderId}
                            message={chat.message}
                            timestamp={chat.timestamp}
                            isPublic={chat.isPublic}
                            isMe={chat.isMe}
                        />
                    ))}
                </div>
                <div className="enter-chat">
                    <div className="message-input-container">
                        <input
                            className="message-input"
                            type="text"
                            ref={chatInputRef}
                        />
                        <button
                            className="send-message"
                            type="button"
                            onClick={() =>
                                chat(
                                    chatInputRef.current!.value,
                                    currentRoomInfo,
                                    thisUser
                                )
                            }
                        >
                            {strResource.room.send}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

async function init(
    thisUser: UserInfo,
    currentRoomInfo: CurrentRoomInfoInitialState,
    usersInRoom: UserInfo[],
    setUsersInRoom: React.Dispatch<React.SetStateAction<UserInfo[]>>,
    setNewUser: React.Dispatch<React.SetStateAction<UserInfo>>,
    setNewChat: React.Dispatch<React.SetStateAction<Chat>>
) {
    const fetchedUsers = await axios.get<CommonResponse<UserResponse[]>>(
        serverSpecResource.restApiUrl +
            serverSpecResource.restEndpoints.gameUser.replace(
                "{gameId}",
                currentRoomInfo.roomInfo.roomId.toString()
            )
    );
    const u: UserInfo[] = [];
    fetchedUsers.data.data.forEach((us) =>
        u.push({
            userId: us.userId,
            username: us.username,
            characterCode: null,
            isDead: false
        })
    );
    setUsersInRoom(u);

    const sock = await SocketClient.getInstance();
    // TODO: store Subscription object returned
    await sock.subscribe(
        `${serverSpecResource.socketEndpoints.subscribe.notificationPublic}/${currentRoomInfo.roomInfo.roomId}`,
        (payload) => {
            const payloadData = JSON.parse(payload.body)
                .body as CommonResponse<UserResponse>;
            const userInfo: UserInfo = {
                userId: payloadData.data.userId,
                username: payloadData.data.username,
                characterCode: null,
                isDead: false
            };
            setNewUser(userInfo);
        }
    );
    await sock.subscribe(
        `${serverSpecResource.socketEndpoints.subscribe.chatroom}/${currentRoomInfo.roomInfo.roomId}`,
        (payload) => {
            const payloadData = JSON.parse(payload.body)
                .body as CommonResponse<PublicChatMessage>;
            console.log(payloadData);

            const chat: Chat = {
                senderId: payloadData.data.senderId,
                message: payloadData.data.message,
                timestamp: new Date(payloadData.dateTime).getTime(),
                isPublic: true,
                isMe: thisUser.userId === payloadData.data.senderId
            };
            setNewChat(chat);
        }
    );
    await sock.subscribe(
        `${serverSpecResource.socketEndpoints.subscribe.chatroom}/${currentRoomInfo.roomInfo.roomId}/private/${thisUser.userId}`,
        (payload) => {
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
    );
    sockClient = sock;
    console.log(sockClient);
}

const chat = (
    message: string,
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo
) => {
    const messageObj: PublicChatMessage = {
        gameId: currentRoomInfo.roomInfo.roomId,
        senderId: thisUser.userId,
        message
    };
    console.log("chat:", sockClient);
    sockClient.sendMessage("/app/chatroom/public-message", {}, messageObj);
};

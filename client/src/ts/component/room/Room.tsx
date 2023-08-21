import {useRef, useState, useEffect} from "react";
import {Chat, GameSetting, UserInfo} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import SocketClient from "../../sockjs/SocketClient";
import {CurrentRoomInfoInitialState} from "../../redux/slice/currentRoomInfoSlice";
import {ChatItem, UserItem} from "../HomeSubcomponents";
import {GameConfigurationModal} from "./RoomSubcomponent";
import {setGameConfigurationModal} from "../../redux/slice/gameConfigurationModal";
import {
    GameStartNotificationRequest,
    UserRequest
} from "../../type/requestType";
import {Subscription} from "stompjs";
import {SOCKET_SEND_GAME_START, SOCKET_SEND_USER_EXIT} from "../../util/const";
import {chat} from "../../util/chat";
import {fetchUsers} from "../../util/fetchUsers";
import GameComponent from "./GameComponent";
import {getSubscription} from "../../util/getSubscription";
import {
    iDelayStateForNewChat,
    iDelayStateForNewUser,
    iNewChat,
    iNewUserState
} from "../../util/initialState";

var sockClient: SocketClient;
var subscriptions: {endpoint: string; subscription: Subscription}[] | undefined;

const EMPTY_NEW_USER = -1;

export type UserState = {
    stateType: "USER_ENTERED" | "USER_EXITED" | null;
    userInfo: UserInfo;
};

export default function Room() {
    const [usersInRoom, setUsersInRoom] = useState<UserInfo[]>([]);
    const [newUserState, setNewUserState] = useState<UserState>(iNewUserState);
    const [delayStateForNewUser, setDelayStateForNewUser] = useState<UserState>(
        iDelayStateForNewUser
    );
    const [chatLogs, setChatLogs] = useState<Array<Chat>>([]);
    const [newChat, setNewChat] = useState<Chat>(iNewChat);
    const [delayStateForNewChat, setDelayStateForNewChat] = useState<Chat>(
        iDelayStateForNewChat
    );
    const gameConfigurationModal = useAppSelector(
        (state) => state.gameConfigurationModal
    );

    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const currentRoomInfo = useAppSelector((state) => state.currentRoomInfo);
    const gameConfiguration = useAppSelector((state) => state.gameConfiugraion);
    const gameStarted = useAppSelector((state) => state.gameSwitch);

    const chatInputRef = useRef<HTMLInputElement>(null);

    const dispatch = useAppDispatch();

    const toSubscribe = getSubscription(
        currentRoomInfo,
        setNewUserState,
        thisUser,
        setNewChat
    );

    useEffect(() => {
        if (!sockClient) {
            init(currentRoomInfo, thisUser, setUsersInRoom, toSubscribe);
        }
        return () => {
            if (sockClient) {
                const exitRequestBody: UserRequest = {
                    notificationType: "USER_REMOVED",
                    gameId: currentRoomInfo.roomInfo.roomId,
                    userId: thisUser.userId,
                    username: thisUser.username
                };
                sockClient.sendMessage(
                    SOCKET_SEND_USER_EXIT,
                    {},
                    exitRequestBody
                );
                if (subscriptions) {
                    subscriptions.forEach((sub) => {
                        sub.subscription.unsubscribe();
                    });
                    subscriptions = undefined;
                }
            }
        };
    }, []);

    useEffect(() => {
        if (newUserState.userInfo.userId !== EMPTY_NEW_USER) {
            if (newUserState.stateType === "USER_EXITED") {
                const mUsersInRoom = usersInRoom.filter(
                    (val) => val.userId !== newUserState.userInfo.userId
                );
                setUsersInRoom([...mUsersInRoom]);
            } else if (
                delayStateForNewUser.userInfo.userId !==
                newUserState.userInfo.userId
            ) {
                if (newUserState.stateType === "USER_ENTERED") {
                    setUsersInRoom([...usersInRoom, newUserState.userInfo]);
                }
                setDelayStateForNewUser({...newUserState});
            }
        }
    }, [newUserState]);

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
            {thisUser.userId === currentRoomInfo.roomInfo.hostId && !gameStarted ? (
                <div className="host-bar">
                    <button
                        type="button"
                        onClick={() => {
                            const body: GameStartNotificationRequest = {
                                notificationType: "GAME_START",
                                gameSetting: gameConfiguration,
                                gameId: currentRoomInfo.roomInfo.roomId
                            };
                            sockClient.sendMessage(
                                SOCKET_SEND_GAME_START,
                                {},
                                body
                            );
                        }}
                    >
                        {strResource.room.gameStart}
                    </button>
                    <button
                        type="button"
                        onClick={() =>
                            dispatch(setGameConfigurationModal(true))
                        }
                    >
                        {strResource.room.gameConfiguration}
                    </button>
                </div>
            ) : null}
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
            {gameStarted ? <GameComponent /> : null}
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
                                    thisUser,
                                    sockClient
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
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo,
    setUsersInRoom: React.Dispatch<React.SetStateAction<UserInfo[]>>,
    toSubscribe: {endpoint: string; callback: (payload: any) => void}[]
) {
    const sock = await SocketClient.getInstance();
    sockClient = sock;

    const u: UserInfo[] = await fetchUsers(currentRoomInfo);
    setUsersInRoom(u);

    if (!subscriptions) {
        subscriptions = [];
    }
    let i = 1;
    for (let sub of toSubscribe) {
        const subscription = await sockClient.subscribe(
            sub.endpoint,
            sub.callback,
            {id: `sub-${i++}`}
        );
        subscriptions.push({endpoint: sub.endpoint, subscription});
    }

    // TODO: Logically, when the host create a room, the client
    // send a post request to the server. The server creates a room and add
    // a user to the room simultaneosly, which the added user is the host.
    // Without the check by if shown below, the host user will be added to the
    // room twice, which I have to prevent.
    // For now, check whether the user is the host which already belongs to the room.
    // Later, I wish make this logic more reasonable.
    if (thisUser.userId !== currentRoomInfo.roomInfo.hostId) {
        const body: UserRequest = {
            notificationType: "USER_ENTERED",
            gameId: currentRoomInfo.roomInfo.roomId,
            userId: thisUser.userId,
            username: thisUser.username
        };
        try {
            await sockClient.sendMessage("/app/game/user-enter", {}, body);
        } catch (err) {
            console.error(err);
        }
    }
}

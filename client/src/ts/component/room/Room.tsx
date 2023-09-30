import {useState, useEffect} from "react";
import {GamePhase, UserInfo} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import SocketClient from "../../sockjs/SocketClient";
import {CurrentRoomInfoInitialState} from "../../redux/slice/currentRoomInfoSlice";
import {GameConfigurationModal, UserItem} from "./RoomSubcomponent";
import {setGameConfigurationModal} from "../../redux/slice/gameConfigurationModal";
import {
    CharacterGenerationRequest,
    GameStartRequest,
    UserRequest
} from "../../type/requestType";
import {Subscription} from "stompjs";
import {
    SOCKET_SEND_GAME_DISTRIBUTE_CHARACTER,
    SOCKET_SEND_GAME_START,
    SOCKET_SEND_USER_EXIT,
    SystemMessengerId
} from "../../util/const";
import {fetchUsers} from "../../util/fetchUsers";
import GameComponent from "./GameComponent";
import {getSubscription} from "../../sockjs/getSubscription";
import {iDelayStateForNewUser, iNewUserState} from "../../util/initialState";
import ChatComponent from "./ChatComponent";
import {setCurrentGamePhase} from "../../redux/slice/currentGamePhaseSlice";
import GameManager from "../../game/GameManager";
import {sumCharacterDistribution} from "../../util/utilFunction";
import {sendSystemMessage} from "../../sockjs/chat";
import "../../../css/Room.css";
import {setUserIdsInRoom} from "../../redux/slice/userIdsInRoomSlice";

var inited = false;
var sockClient: SocketClient | undefined;
var subscriptions: {endpoint: string; subscription: Subscription}[] | undefined;
var gameManager = GameManager.getInstance();

const EMPTY_NEW_USER = -1;

export type UserState = {
    stateType: "USER_ENTERED" | "USER_EXITED" | null;
    userInfo: UserInfo;
};

export default function Room() {
    const [newUserState, setNewUserState] = useState<UserState>(iNewUserState);
    const [delayStateForNewUser, setDelayStateForNewUser] = useState<UserState>(
        iDelayStateForNewUser
    );
    const [sumOfCharacterDistribution, setSumOfCharacterDistribution] =
        useState<number>(0);

    const gameConfigurationModal = useAppSelector(
        (state) => state.gameConfigurationModal
    );

    const userIdsInRoom = useAppSelector((state) => state.userIdsInRoom);
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const currentRoomInfo = useAppSelector((state) => state.currentRoomInfo);
    const gameConfiguration = useAppSelector((state) => state.gameConfiugraion);
    const characterDistribution = useAppSelector(
        (state) => state.characterDistribution
    );
    const gameStarted = useAppSelector((state) => state.gameSwitch);
    const currentGamePhase = useAppSelector((state) => state.currentGamePhase);

    const dispatch = useAppDispatch();

    const toSubscribe = getSubscription(
        currentRoomInfo,
        setNewUserState,
        thisUser,
        dispatch
    );

    useEffect(() => {
        if (!inited) {
            if (!sockClient) {
                init(currentRoomInfo, thisUser, dispatch, toSubscribe).then(
                    (result) => (inited = true)
                );
                gameManager.gameId = currentRoomInfo.roomInfo.roomId;
                gameManager.dispatch = dispatch;
            }
        }
        return () => {
            if (sockClient) {
                sendSystemMessage(
                    thisUser.username +
                        strResource.notificationMessage.someoneExited,
                    SystemMessengerId.USER_EXITED,
                    currentRoomInfo
                );
                const exitRequestBody: UserRequest = {
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
                sockClient.disconnect();
                sockClient = undefined;
                inited = false;
            }
        };
    }, []);

    useEffect(() => {
        if (newUserState.userInfo.userId !== EMPTY_NEW_USER) {
            if (newUserState.stateType === "USER_EXITED") {
                const modifiedUserIdsInRoom = userIdsInRoom.filter(
                    (userId) => userId !== newUserState.userInfo.userId
                );
                const newUserIdList = [...modifiedUserIdsInRoom];
                dispatch(setUserIdsInRoom(newUserIdList));
                gameManager.userIdsInRoom = newUserIdList;
                gameManager.removeUser(newUserState.userInfo.userId);
            } else if (
                delayStateForNewUser.userInfo.userId !==
                newUserState.userInfo.userId
            ) {
                if (newUserState.stateType === "USER_ENTERED") {
                    const newUserIdList = [
                        ...userIdsInRoom,
                        newUserState.userInfo.userId
                    ];
                    dispatch(setUserIdsInRoom(newUserIdList));
                    gameManager.userIdsInRoom = newUserIdList;
                    gameManager.setUser(newUserState.userInfo);
                }
                setDelayStateForNewUser({...newUserState});
            }
        }
    }, [newUserState]);

    useEffect(() => {
        gameManager.userId = thisUser.userId;
        gameManager.username = thisUser.username;
    }, [thisUser]);

    useEffect(() => {
        const sum = sumCharacterDistribution(characterDistribution);
        setSumOfCharacterDistribution(sum);
    }, [characterDistribution]);

    return (
        <div className="room-container">
            {gameConfigurationModal && !gameStarted ? (
                <GameConfigurationModal
                    characterConfigurationProps={{userIdsInRoom}}
                />
            ) : null}
            <p className="room-name">{currentRoomInfo.roomInfo.roomName}</p>
            <div className="room-content">
                <ul className="user-list">
                    {userIdsInRoom.map((id, idx) => {
                        const user = gameManager.getUser(id)!;
                        return (
                            <UserItem
                                key={`user-item-${idx}`}
                                userId={user.userId}
                                username={user.username}
                                characterCode={user.characterCode}
                                isDead={user.isDead}
                            />
                        );
                    })}
                </ul>
                <ChatComponent userIds={userIdsInRoom} inited={inited} />
                <div className="game-container">
                    {thisUser.userId === currentRoomInfo.roomInfo.hostId &&
                    !gameStarted ? (
                        <div className="host-bar">
                            <button
                                className="btn--game-start"
                                type="button"
                                onClick={async () => {
                                    const gameStartNotificationRequestBody: GameStartRequest =
                                        {
                                            gameSetting: gameConfiguration,
                                            gameId: currentRoomInfo.roomInfo
                                                .roomId
                                        };
                                    if (!sockClient) {
                                        sockClient =
                                            await SocketClient.getInstance();
                                    }
                                    sockClient.sendMessage(
                                        SOCKET_SEND_GAME_START,
                                        {},
                                        gameStartNotificationRequestBody
                                    );

                                    dispatch(
                                        setCurrentGamePhase(
                                            GamePhase.CHARACTER_DISTRIBUTION
                                        )
                                    );
                                    const characterDistributionRequestBody: CharacterGenerationRequest =
                                        {
                                            gameId: currentRoomInfo.roomInfo
                                                .roomId,
                                            characterDistribution
                                        };
                                    sockClient.sendMessage(
                                        SOCKET_SEND_GAME_DISTRIBUTE_CHARACTER,
                                        {},
                                        characterDistributionRequestBody
                                    );
                                }}
                                disabled={
                                    userIdsInRoom.length !==
                                    sumOfCharacterDistribution
                                }
                            >
                                {strResource.room.gameStart}
                            </button>
                            <button
                                className="btn--config-modal"
                                type="button"
                                onClick={() =>
                                    dispatch(setGameConfigurationModal(true))
                                }
                            >
                                {strResource.room.gameConfiguration}
                            </button>
                        </div>
                    ) : null}
                    {gameStarted ? <GameComponent /> : null}
                </div>
            </div>
        </div>
    );
}

async function init(
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo,
    dispatch: any,
    toSubscribe: {endpoint: string; callback: (payload: any) => void}[]
) {
    const sock = await SocketClient.getInstance();
    sockClient = sock;

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

    const u: UserInfo[] = await fetchUsers(currentRoomInfo);
    const userIds = u.map((user) => user.userId);
    const users = new Map<number, UserInfo>();
    u.forEach((user) => users.set(user.userId, user));
    dispatch(setUserIdsInRoom(userIds));
    gameManager.usersInRoom = users;

    // TODO: Logically, when the host create a room, the client
    // send a post request to the server. The server creates a room and add
    // a user to the room simultaneosly, which the added user is the host.
    // Without the check by if shown below, the host user will be added to the
    // room twice, which I have to prevent.
    // For now, check whether the user is the host which already belongs to the room.
    // Later, I wish make this logic more reasonable.
    if (thisUser.userId !== currentRoomInfo.roomInfo.hostId) {
        const body: UserRequest = {
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

    sendSystemMessage(
        thisUser.username + strResource.notificationMessage.someoneEntered,
        SystemMessengerId.USER_ENTERED,
        currentRoomInfo
    );
}

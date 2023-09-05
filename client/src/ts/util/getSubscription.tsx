import {
    Chat,
    GamePhase,
    GameSetting,
    PrivateChatMessage,
    PublicChatMessage,
    UserInfo
} from "../type/gameDomainType";
import {
    CharacterGenerationResponse,
    CommonResponse,
    GameStartNotificationResponse,
    UserResponse
} from "../type/responseType";
import {CurrentRoomInfoInitialState} from "../redux/slice/currentRoomInfoSlice";
import {
    GAME_PHASE_ORDER,
    SOCKET_SEND_PHASE_END,
    SOCKET_SUBSCRIBE_CHATROOM_PRIVATE,
    SOCKET_SUBSCRIBE_CHATROOM_PUBLIC,
    SOCKET_SUBSCRIBE_NOTIFICATION_PRIVATE,
    SOCKET_SUBSCRIBE_NOTIFICATION_PUBLIC
} from "./const";
import {UserState} from "../component/room/Room";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import {setGameSwitch} from "../redux/slice/GameSwitchSlice";
import {setGameConfiguration} from "../redux/slice/gameConfiguration";
import GameManager from "../game/GameManager";
import SocketClient from "../sockjs/SocketClient";
import {PhaseEndRequest} from "../type/requestType";
import {NotificationType} from "../type/notificationType";
import {setCurrentGamePhase} from "../redux/slice/currentGamePhaseSlice";
import {setTimeCount} from "../redux/slice/timeCountSlice";

var gameManager = GameManager.getInstance();

export function getSubscription(
    currentRoomInfo: CurrentRoomInfoInitialState,
    setNewUserState: React.Dispatch<React.SetStateAction<UserState>>,
    thisUser: UserInfo,
    setNewChat: React.Dispatch<React.SetStateAction<Chat>>,
    currentGamePhase: {value: GamePhase},
    gameConfiguration: GameSetting,
    dispatch: any
): {endpoint: string; callback: (payload: any) => void}[] {
    return [
        {
            endpoint: `${SOCKET_SUBSCRIBE_NOTIFICATION_PUBLIC(
                currentRoomInfo.roomInfo.roomId
            )}`,
            callback: (payload: any) => {
                const payloadData = JSON.parse(payload.body)
                    .body as CommonResponse<any>;
                const notificationType = payloadData.data
                    .notificationType as NotificationType;
                switch (notificationType) {
                    case "USER_ENTERED":
                    case "USER_REMOVED":
                        onUserEnterOrExit(payloadData, setNewUserState);
                        break;
                    case "GAME_START":
                        onGameStart(payloadData, dispatch);
                        break;
                    case "PHASE_END":
                        onPhaseEnd(
                            currentGamePhase.value,
                            dispatch,
                            gameConfiguration
                        );
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
        },
        {
            endpoint: `${SOCKET_SUBSCRIBE_NOTIFICATION_PRIVATE(
                currentRoomInfo.roomInfo.roomId,
                thisUser.userId
            )}`,
            callback: (payload: any) => {
                const payloadData = JSON.parse(payload.body)
                    .body as CommonResponse<CharacterGenerationResponse>;
                switch (payloadData.data.notificationType) {
                    case "NOTIFY_WHICH_CHARACTER_ALLOCATED":
                        onCharacterAllocationResponse(
                            dispatch,
                            currentRoomInfo,
                            thisUser,
                            payloadData
                        );
                        break;
                }
            }
        }
    ];
}

function onUserEnterOrExit(
    payloadData: CommonResponse<UserResponse>,
    setNewUserState: React.Dispatch<React.SetStateAction<UserState>>
) {
    const userInfo: UserInfo = {
        userId: payloadData.data.userId,
        username: payloadData.data.username,
        characterCode: null,
        isDead: false
    };
    setNewUserState({
        stateType:
            payloadData.data.notificationType === "USER_ENTERED"
                ? "USER_ENTERED"
                : payloadData.data.notificationType === "USER_REMOVED"
                ? "USER_EXITED"
                : null,
        userInfo
    });
}

function onGameStart(
    payloadData: CommonResponse<GameStartNotificationResponse>,
    dispatch: any
) {
    const gameSetting = payloadData.data.gameSetting;

    const gameManager = GameManager.getInstance();
    gameManager.gameSetting = gameSetting;
    gameManager.currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
    dispatch(setGameSwitch(true));

    dispatch(
        setGameConfiguration({
            discussionTimeSeconds: gameSetting.discussionTimeSeconds,
            voteTimeSeconds: gameSetting.voteTimeSeconds,
            nightTimeSeconds: gameSetting.nightTimeSeconds
        })
    );
}

async function onCharacterAllocationResponse(
    dispatch: any,
    currentRoomInfo: CurrentRoomInfoInitialState,
    thisUser: UserInfo,
    payloadData: CommonResponse<CharacterGenerationResponse>
) {
    dispatch(
        setThisUserInfo({
            ...thisUser,
            characterCode: payloadData.data.characterCode
        })
    );
    const sockClient = await SocketClient.getInstance();
    const body: PhaseEndRequest = {
        notificationType: "PHASE_END",
        gameId: currentRoomInfo.roomInfo.roomId,
        userId: thisUser.userId
    };
    sockClient.sendMessage(SOCKET_SEND_PHASE_END, {}, body);
}

function onPhaseEnd(
    currentGamePhase: GamePhase,
    dispatch: any,
    gameConfiguartion: GameSetting
) {
    const nextPhase = getNextPhase(currentGamePhase);
    dispatch(setCurrentGamePhase(nextPhase));
    taskOnNextPhase(nextPhase, dispatch);
}

function getNextPhase(currentGamePhase: GamePhase) {
    const idx = GAME_PHASE_ORDER.indexOf(currentGamePhase);
    const nextIdx = (idx % (GAME_PHASE_ORDER.length - 1)) + 1;
    return GAME_PHASE_ORDER[nextIdx];
}

function taskOnNextPhase(
    nextPhase: GamePhase,
    dispatch: any
) {
    const gameConfiguration = gameManager.gameSetting;
    let howMany;
    switch (nextPhase) {
        case GamePhase.DAYTIME:
            howMany = gameConfiguration.discussionTimeSeconds;
            dispatch(setTimeCount(howMany));
            startTimeCount(howMany, dispatch);
            break;
        case GamePhase.VOTE:
            howMany = gameConfiguration.voteTimeSeconds;
            dispatch(setTimeCount(howMany));
            startTimeCount(howMany, dispatch);
            break;
        case GamePhase.NIGHT:
            howMany = gameConfiguration.nightTimeSeconds;
            dispatch(setTimeCount(howMany));
            startTimeCount(howMany, dispatch);
            break;
        default:
            const DEFAULT_COUNT = 90;
            dispatch(setTimeCount(DEFAULT_COUNT));
            startTimeCount(DEFAULT_COUNT, dispatch);
    }
}

function startTimeCount(count: number, dispatch: any) {
    if (count <= 0) {
        return;
    }

    const SECOND_IN_MILLIS = 1000;
    setTimeout(() => {
        const c = count - 1;
        dispatch(setTimeCount(c));
        startTimeCount(c, dispatch);
    }, SECOND_IN_MILLIS);
}

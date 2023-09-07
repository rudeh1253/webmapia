import {
    Chat,
    PrivateChatMessage,
    PublicChatMessage,
    UserInfo
} from "../type/gameDomainType";
import {
    CharacterGenerationResponse,
    CommonResponse,
    GameStartNotificationResponse,
    PhaseResultResponse,
    UserResponse
} from "../type/responseType";
import {CurrentRoomInfoInitialState} from "../redux/slice/currentRoomInfoSlice";
import {
    ID_OF_PUBLIC_CHAT,
    SOCKET_SEND_PHASE_END,
    SOCKET_SUBSCRIBE_CHATROOM_PRIVATE,
    SOCKET_SUBSCRIBE_CHATROOM_PUBLIC,
    SOCKET_SUBSCRIBE_NOTIFICATION_PRIVATE,
    SOCKET_SUBSCRIBE_NOTIFICATION_PUBLIC
} from "../util/const";
import {UserState} from "../component/room/Room";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import GameManager from "../game/GameManager";
import SocketClient from "./SocketClient";
import {PhaseEndRequest} from "../type/requestType";
import {NotificationType} from "../type/notificationType";
import NullPointerError from "../error/NullPointerError";
import {ErrorCode} from "../error/ErrorCode";
import NotAssignedError from "../error/NotAssignedError";
import {setNewChat} from "../redux/slice/newChatSlice";
import {onNewChatContainerCreated} from "./chat";

var gameManager = GameManager.getInstance();

export function getSubscription(
    currentRoomInfo: CurrentRoomInfoInitialState,
    setNewUserState: React.Dispatch<React.SetStateAction<UserState>>,
    thisUser: UserInfo,
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
                        postprocessOfPhase(currentRoomInfo);
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
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: thisUser.userId === payloadData.data.senderId
                };
                dispatch(setNewChat(chat));
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
                    containerId: payloadData.data.containerId,
                    isMe: thisUser.userId === payloadData.data.senderId
                };
                dispatch(setNewChat(chat));
            }
        },
        {
            endpoint: `${SOCKET_SUBSCRIBE_NOTIFICATION_PRIVATE(
                currentRoomInfo.roomInfo.roomId,
                thisUser.userId
            )}`,
            callback: (payload: any) => {
                const payloadData = JSON.parse(payload.body)
                    .body as CommonResponse<any>;
                switch (payloadData.data.notificationType as NotificationType) {
                    case "NOTIFY_WHICH_CHARACTER_ALLOCATED":
                        onCharacterAllocationResponse(
                            dispatch,
                            currentRoomInfo,
                            thisUser,
                            payloadData
                        );
                        break;
                    case "PHASE_RESULT":
                        const gameEnded = processPhaseResult(payloadData.data);
                        if (!gameEnded) {
                            startNewPhase(dispatch);
                        }
                        break;
                    case "CREATE_NEW_CHAT_CONTAINER":
                        onNewChatContainerCreated(payloadData.data, dispatch);
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
    const gameManager = GameManager.getInstance();
    const gameSetting = payloadData.data.gameSetting;
    try {
        gameManager.gameStart(gameSetting);
    } catch (err) {
        if (err instanceof NullPointerError) {
            if (err.errorCode === ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER) {
                gameManager.dispatch = dispatch;
                gameManager.gameStart(gameSetting);
            }
        }
    }
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
        gameId: currentRoomInfo.roomInfo.roomId,
        userId: thisUser.userId
    };
    sockClient.sendMessage(SOCKET_SEND_PHASE_END, {}, body);
}

function postprocessOfPhase(currentRoomInfo: CurrentRoomInfoInitialState) {
    try {
        gameManager.postPhase();
    } catch (err) {
        if (err instanceof NotAssignedError) {
            if (
                err.errorCode === ErrorCode.GAME_ID_NOT_ASSIGNED_IN_GAME_MANAGER
            ) {
                gameManager.gameId = currentRoomInfo.roomInfo.roomId;
                gameManager.postPhase();
            }
        }
    }
}

function processPhaseResult(response: PhaseResultResponse) {
    return gameManager.processPhaseResult(response);
}

function startNewPhase(dispatch: any) {
    try {
        gameManager.moveToNextPhase();
    } catch (err) {
        if (err instanceof NullPointerError) {
            if (err.errorCode === ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER) {
                gameManager.dispatch = dispatch;
                gameManager.moveToNextPhase();
            }
        }
    }
    try {
        gameManager.taskOnNewPhase();
    } catch (err) {
        if (err instanceof NullPointerError) {
            if (err.errorCode === ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER) {
                gameManager.dispatch = dispatch;
                gameManager.taskOnNewPhase();
            }
        }
    }
}

import {ErrorCode} from "../error/ErrorCode";
import NotAssignedError from "../error/NotAssignedError";
import NullPointerError from "../error/NullPointerError";
import {setGameSwitch} from "../redux/slice/GameSwitchSlice";
import {setCurrentGamePhase} from "../redux/slice/currentGamePhaseSlice";
import {setGameConfiguration} from "../redux/slice/gameConfiguration";
import {setTimeCount} from "../redux/slice/timeCountSlice";
import SocketClient from "../sockjs/SocketClient";
import {
    CharacterCode,
    Chat,
    GamePhase,
    GameSetting,
    UserInfo
} from "../type/gameDomainType";
import {PhaseEndRequest} from "../type/requestType";
import {
    CommonResponse,
    PhaseResultResponse,
    UserResponse
} from "../type/responseType";
import {
    DEFAULT_TIME_CONFIGURATION,
    GAME_PHASE_ORDER,
    ID_OF_PUBLIC_CHAT,
    SystemMessengerId,
    SOCKET_SEND_PHASE_END,
    REST_ONE_GAME_USER,
    ID_OF_WOLF_CHAT,
    NAME_OF_WOLF_CHAT,
    ID_OF_CHAT_FOR_DEAD,
    NAME_OF_CHAT_FOR_DEAD,
    ID_OF_SECRET_SOCIETY_CHAT,
    NAME_OF_SECRET_SOCIETY_CHAT,
    REST_GAME_USER
} from "../util/const";
import strResource from "../../resource/string.json";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import {setNewChat} from "../redux/slice/newChatSlice";
import axios from "axios";
import {characterNameMap} from "./characterNameMap";
import {participateChatContainer} from "../sockjs/chat";
import {setUserIdsInRoom} from "../redux/slice/userIdsInRoomSlice";
import {setPhaseResultInfo} from "../redux/slice/phaseResultInfoSlice";

var sockClient: SocketClient;

export default class GameManager {
    private static singleton: GameManager;

    private _thisUser: UserInfo;
    private _gameId: number;
    private _gameSetting: GameSetting;
    private _currentGamePhase: GamePhase;
    private _dispatch: any;
    private _usersInRoom: Map<number, UserInfo>;
    private _userIdsInRoom: number[];
    private _zero: number;

    private constructor() {
        this._thisUser = {
            userId: -1,
            username: "",
            characterCode: null,
            isDead: false
        };
        this._gameId = 0;
        this._gameSetting = DEFAULT_TIME_CONFIGURATION;
        this._currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
        this._dispatch = null;
        this._usersInRoom = new Map<number, UserInfo>();
        this._userIdsInRoom = [];
        this._zero = 0;
    }

    public static getInstance(): GameManager {
        if (this.singleton == null) {
            this.singleton = new GameManager();
        }
        return this.singleton;
    }

    public set gameSetting(gameSetting: GameSetting) {
        this._gameSetting = gameSetting;
    }

    public get gameSetting() {
        return this._gameSetting;
    }

    public set currentGamePhase(gamePhase: GamePhase) {
        this._currentGamePhase = gamePhase;
    }

    public get currentGamePhase() {
        return this._currentGamePhase;
    }

    public set dispatch(dispatch: any) {
        this._dispatch = dispatch;
    }

    public get dispatch() {
        return this._dispatch;
    }

    public set gameId(gameId: number) {
        this._gameId = gameId;
    }

    public get userId() {
        return this._thisUser.userId;
    }

    public set userId(userId: number) {
        this._thisUser = {
            ...this._thisUser,
            userId
        };
    }

    public get isDead() {
        return this._thisUser.isDead;
    }

    public set isDead(isDead: boolean) {
        this._thisUser.isDead = isDead;
    }

    public get characterCode() {
        return this._thisUser.characterCode;
    }

    public set characterCode(characterCode: CharacterCode) {
        this._thisUser.characterCode = characterCode;
    }

    public get username() {
        return this._thisUser.username;
    }

    public set username(username: string) {
        this._thisUser.username = username;
    }

    public set usersInRoom(usersInRoom: Map<number, UserInfo>) {
        this._usersInRoom = usersInRoom;
    }

    public get usersInRoom() {
        return this._usersInRoom;
    }

    public get userIdsInRoom() {
        return this._userIdsInRoom;
    }

    public set userIdsInRoom(userIdsInRoom: number[]) {
        this._userIdsInRoom = userIdsInRoom;
    }

    public setUser(user: UserInfo) {
        this._usersInRoom.set(user.userId, user);
    }

    public removeUser(key: number) {
        return this._usersInRoom.delete(key);
    }

    public getUser(key: number) {
        return this._usersInRoom.get(key);
    }

    public gameStart(gameSetting: GameSetting) {
        if (this._dispatch === null) {
            throw new NullPointerError(
                ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER
            );
        }
        this._gameSetting = gameSetting;
        this._currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
        this._dispatch(setGameSwitch(true));
        this._dispatch(
            setGameConfiguration({
                discussionTimeSeconds: gameSetting.discussionTimeSeconds,
                voteTimeSeconds: gameSetting.voteTimeSeconds,
                nightTimeSeconds: gameSetting.nightTimeSeconds
            })
        );
        const startMessage: Chat = {
            senderId: SystemMessengerId.GAME_STARTED,
            message: strResource.game.gameStarted,
            timestamp: new Date().getTime(),
            containerId: ID_OF_PUBLIC_CHAT,
            isMe: false
        };
        this._dispatch(setNewChat([startMessage]));
    }

    public async processPhaseResult(data: PhaseResultResponse) {
        if (data.gameEnd) {
            const users = await axios.get<CommonResponse<UserResponse[]>>(
                REST_GAME_USER(this._gameId)
            );
            this._dispatch(
                setPhaseResultInfo({
                    winner: data.winner,
                    users: users.data.data
                })
            );
            this._dispatch(setCurrentGamePhase(GamePhase.GAME_END));
        }
        const newChatList: Chat[] = [];
        switch (data.endedPhase) {
            case GamePhase.VOTE:
                const voteResult = data.voteResult;
                if (voteResult.notificationType === "EXECUTE_BY_VOTE") {
                    const user = this._usersInRoom.get(
                        voteResult.idOfUserToBeExecuted
                    );
                    this._usersInRoom.set(voteResult.idOfUserToBeExecuted, {
                        ...user!,
                        isDead: true
                    });
                    this._dispatch(setUserIdsInRoom([...this._userIdsInRoom]));
                    if (
                        this._thisUser.userId ===
                        voteResult.idOfUserToBeExecuted
                    ) {
                        this._thisUser.isDead = true;
                        this._dispatch(
                            setThisUserInfo({
                                ...this._thisUser
                            })
                        );
                        await participateChatContainer(
                            this._gameId,
                            ID_OF_CHAT_FOR_DEAD,
                            NAME_OF_CHAT_FOR_DEAD,
                            this._thisUser.userId
                        );
                    }
                    const userRequestResult = await axios.get<
                        CommonResponse<UserResponse>
                    >(
                        REST_ONE_GAME_USER(
                            this._gameId,
                            voteResult.idOfUserToBeExecuted
                        )
                    );
                    const userResponse = userRequestResult.data.data;
                    const userToExecuted: UserInfo = {
                        userId: userResponse.userId,
                        username: userResponse.username,
                        characterCode: userResponse.characterCode,
                        isDead: userResponse.isDead
                    };
                    newChatList.push({
                        senderId: SystemMessengerId.SOMEONE_WAS_EXECUTED,
                        message: `${userToExecuted.username}${strResource.notificationMessage.someoneExecuted}`,
                        timestamp: new Date().getTime(),
                        containerId: ID_OF_PUBLIC_CHAT,
                        isMe: false
                    });
                } else {
                    newChatList.push({
                        senderId: SystemMessengerId.NONE_WAS_EXECUTED,
                        message:
                            strResource.notificationMessage.noneWasExecuted,
                        timestamp: new Date().getTime(),
                        containerId: ID_OF_PUBLIC_CHAT,
                        isMe: false
                    });
                }
                break;
            case GamePhase.NIGHT:
                console.log("data:", data);
                for (let result of data.skillResults) {
                    let deadUser: UserInfo;
                    let senderId;
                    let message;
                    switch (result.characterEffectAfterNightType) {
                        case "EXTERMINATE":
                        case "KILL":
                            const deadUserCommonResponse = await axios.get<
                                CommonResponse<UserResponse>
                            >(
                                REST_ONE_GAME_USER(
                                    this._gameId,
                                    result.skillTargetId
                                )
                            );
                            deadUser = {
                                userId: deadUserCommonResponse.data.data.userId,
                                username:
                                    deadUserCommonResponse.data.data.username,
                                characterCode:
                                    deadUserCommonResponse.data.data
                                        .characterCode,
                                isDead: deadUserCommonResponse.data.data.isDead
                            };
                            const user = this._usersInRoom.get(
                                result.skillTargetId
                            );
                            this._usersInRoom.set(user!.userId, {
                                ...user!,
                                isDead: true
                            });
                            this._dispatch(
                                setUserIdsInRoom(this._userIdsInRoom)
                            );
                            if (
                                this._thisUser.userId === result.skillTargetId
                            ) {
                                this._thisUser.isDead = true;
                                this._dispatch(
                                    setThisUserInfo({
                                        ...this._thisUser
                                    })
                                );
                                senderId = SystemMessengerId.YOU_WERE_KILLED;
                                message =
                                    strResource.notificationMessage
                                        .youWereKilled;
                                await participateChatContainer(
                                    this._gameId,
                                    ID_OF_CHAT_FOR_DEAD,
                                    NAME_OF_CHAT_FOR_DEAD,
                                    this._thisUser.userId
                                );
                            } else {
                                senderId =
                                    SystemMessengerId.SOMEONE_WAS_EXTERMINATED;
                                message = `${deadUser.username}${strResource.notificationMessage.someoneKilled}`;
                            }
                            newChatList.push({
                                senderId,
                                message,
                                timestamp: new Date().getTime(),
                                containerId: ID_OF_PUBLIC_CHAT,
                                isMe: false
                            });
                            break;
                        case "INVESTIGATE":
                            if (
                                this._thisUser.userId ===
                                result.skillActivatorId
                            ) {
                                const targetUser: UserInfo = {
                                    userId: result.skillTargetId,
                                    username: result.skillTargetUsername,
                                    characterCode:
                                        result.skillTargetCharacterCode,
                                    isDead: false
                                };
                                newChatList.push({
                                    senderId:
                                        SystemMessengerId.INVESTIGATION_RESULT,
                                    message: `${targetUser.username}${
                                        strResource.notificationMessage.heIs
                                    } ${characterNameMap.get(
                                        targetUser.characterCode
                                    )}${strResource.notificationMessage.wasHe}`,
                                    timestamp: new Date().getTime(),
                                    containerId: ID_OF_PUBLIC_CHAT,
                                    isMe: false
                                });
                            }
                            break;
                        case "GUARD":
                            newChatList.push({
                                senderId: SystemMessengerId.GUARD_SUCCESS,
                                message:
                                    strResource.notificationMessage
                                        .succeededToGuard,
                                timestamp: new Date().getTime(),
                                containerId: ID_OF_PUBLIC_CHAT,
                                isMe: false
                            });
                            break;
                        case "FAIL_TO_KILL":
                            newChatList.push({
                                senderId: SystemMessengerId.SKILL_FAIL,
                                message:
                                    strResource.notificationMessage
                                        .failedToKill,
                                timestamp: new Date().getTime(),
                                containerId: ID_OF_PUBLIC_CHAT,
                                isMe: false
                            });
                            break;
                        case "FAIL_TO_INVESTIGATE":
                            newChatList.push({
                                senderId: SystemMessengerId.SKILL_FAIL,
                                message:
                                    strResource.notificationMessage
                                        .failedToInvestigate,
                                timestamp: new Date().getTime(),
                                containerId: ID_OF_PUBLIC_CHAT,
                                isMe: false
                            });
                            break;
                        case "FAIL_TO_GUARD":
                            newChatList.push({
                                senderId: SystemMessengerId.SKILL_FAIL,
                                message:
                                    strResource.notificationMessage
                                        .failedToGuard,
                                timestamp: new Date().getTime(),
                                containerId: ID_OF_PUBLIC_CHAT,
                                isMe: false
                            });
                            break;
                        case "ENTER_WOLF_CHAT":
                        case "NOTIFY":
                            await participateChatContainer(
                                this._gameId,
                                ID_OF_WOLF_CHAT,
                                NAME_OF_WOLF_CHAT,
                                result.receiverId
                            );
                            break;
                        case "NONE":
                            break;
                        default:
                    }
                }
                break;
        }
        this._dispatch(setNewChat(newChatList));
        return data.gameEnd;
    }

    public moveToNextPhase() {
        if (this._dispatch === null) {
            throw new NullPointerError(
                ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER
            );
        }
        const nextPhase = this.getNextPhase(this._currentGamePhase);
        this._dispatch(setCurrentGamePhase(nextPhase));
        this._currentGamePhase = nextPhase;
        console.log(this._currentGamePhase);

        switch (nextPhase) {
            case GamePhase.NIGHT:
                this._dispatch(
                    setNewChat([
                        {
                            senderId: SystemMessengerId.NIGHT_STARTED,
                            message: strResource.phaseMessage.nightStarted,
                            timestamp: new Date().getTime(),
                            containerId: ID_OF_PUBLIC_CHAT,
                            isMe: false
                        }
                    ])
                );
                break;
            case GamePhase.DAYTIME:
                this._dispatch(
                    setNewChat([
                        {
                            senderId: SystemMessengerId.DAYTIME_STARTED,
                            message: strResource.phaseMessage.daytimeStarted,
                            timestamp: new Date().getTime(),
                            containerId: ID_OF_PUBLIC_CHAT,
                            isMe: false
                        }
                    ])
                );
                break;
            case GamePhase.VOTE:
                this._dispatch(
                    setNewChat([
                        {
                            senderId: SystemMessengerId.VOTE_STARTED,
                            message: strResource.phaseMessage.voteStarted,
                            timestamp: new Date().getTime(),
                            containerId: ID_OF_PUBLIC_CHAT,
                            isMe: false
                        }
                    ])
                );
                break;
        }
    }

    private getNextPhase(currentGamePhase: GamePhase) {
        const idx = GAME_PHASE_ORDER.indexOf(currentGamePhase);
        const nextIdx = (idx % (GAME_PHASE_ORDER.length - 1)) + 1;
        return GAME_PHASE_ORDER[nextIdx];
    }

    public taskOnNewPhase() {
        if (this._dispatch === null) {
            throw new NullPointerError(
                ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER
            );
        }
        const currentPhase = this._currentGamePhase;
        const gameConfig = this._gameSetting;
        const DEFAULT_COUNT = 10;
        let howMany: number = DEFAULT_COUNT;
        switch (currentPhase) {
            case GamePhase.DAYTIME:
                howMany = gameConfig.discussionTimeSeconds;
                break;
            case GamePhase.VOTE:
                howMany = gameConfig.voteTimeSeconds;
                break;
            case GamePhase.NIGHT:
                howMany = gameConfig.nightTimeSeconds;
                break;
            case GamePhase.EXECUTION:
                break;
        }
        this._dispatch(setTimeCount(howMany));
        // TODO
        // this.startCountDown(howMany);
    }

    // TODO: Temporary method for testing
    public manualEnd() {
        // this._zero = 1000;
        this.endPhase();
    }

    private startCountDown(count: number) {
        if (count <= this._zero) {
            this.endPhase();
            return;
        }

        const SECOND_IN_MILLIS = 1000;
        setTimeout(() => {
            const c = count - 1;
            this._dispatch(setTimeCount(c));
            this.startCountDown(c);
        }, SECOND_IN_MILLIS);
    }

    private async endPhase() {
        this._zero = 0;
        if (this._gameId === 0) {
            throw new NotAssignedError(
                ErrorCode.GAME_ID_NOT_ASSIGNED_IN_GAME_MANAGER
            );
        }
        if (this._thisUser.userId === -1) {
            throw new NotAssignedError(
                ErrorCode.USER_ID_NOT_ASSIGNED_IN_GAME_MANAGER
            );
        }
        if (!this._thisUser.isDead) {
            if (!sockClient) {
                sockClient = await SocketClient.getInstance();
            }
            const body: PhaseEndRequest = {
                gameId: this._gameId,
                userId: this._thisUser.userId,
                gamePhase: this._currentGamePhase
            };
            sockClient.sendMessage(SOCKET_SEND_PHASE_END, {}, body);
        }
    }
}

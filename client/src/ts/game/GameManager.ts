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
import {NewParticipantRequest, PhaseEndRequest} from "../type/requestType";
import {
    CommonResponse,
    PhaseResultResponse,
    SkillResultResponse,
    UserResponse
} from "../type/responseType";
import {
    DEFAULT_TIME_CONFIGURATION,
    GAME_PHASE_ORDER,
    ID_OF_PUBLIC_CHAT,
    SystemMessageType,
    SOCKET_SEND_PHASE_END,
    SYSTEM_MESSAGE_ID,
    REST_ONE_GAME_USER,
    ID_OF_WOLF_CHAT,
    SOCKET_SEND_NEW_PARTICIPANT_IN_CHAT,
    NAME_OF_WOLF_CHAT
} from "../util/const";
import strResource from "../../resource/string.json";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import {setNewChat} from "../redux/slice/newChatSlice";
import axios from "axios";
import {characterNameMap} from "./characterNameMap";
import { participateChatContainer } from "../sockjs/chat";

var sockClient: SocketClient;

export default class GameManager {
    private static singleton: GameManager;

    private _thisUser: UserInfo;
    private _gameId: number;
    private _gameSetting: GameSetting;
    private _currentGamePhase: GamePhase;
    private _dispatch: any;
    private _usersInRoom: UserInfo[];

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
        this._usersInRoom = [];
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

    public set usersInRoom(usersInRoom: UserInfo[]) {
        this._usersInRoom = usersInRoom;
    }

    public get usersInRoom() {
        return this._usersInRoom;
    }

    public gameStart(gameSetting: GameSetting) {
        if (this._dispatch === null) {
            throw new NullPointerError(
                ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER
            );
        }
        console.log(gameSetting);
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
    }

    public async processPhaseResult(data: PhaseResultResponse) {
        if (data.gameEnd) {
            this._dispatch(setCurrentGamePhase(GamePhase.GAME_END));
        }
        const newChatList = [];
        switch (data.endedPhase) {
            case GamePhase.VOTE:
                break;
            case GamePhase.NIGHT:
                console.log("processPhaseResult:", "NIGHT");
                console.log("data:", data);
                for (let result of data.skillResults) {
                    console.log("data:", result);
                    console.log("thisUser:", this._thisUser);
                    console.log(
                        "thisUser.userId === result.skillTargetId:",
                        result.skillActivatorId === this._thisUser.userId
                    );
                    if (result.skillActivatorId === this._thisUser.userId) {
                        const newChat = await this.processSkillEffectForMe(
                            result
                        );
                        newChatList.push(newChat);
                    } else {
                        const newChat = await this.processSkillEffectForOther(
                            result
                        );
                        newChatList.push(newChat);
                    }
                }
                break;
        }
        this._dispatch(setNewChat(newChatList));
        console.log("here");
        return data.gameEnd;
    }

    private async processSkillEffectForMe(
        result: SkillResultResponse
    ): Promise<Chat> {
        console.log(result);
        switch (result.characterEffectAfterNightType) {
            case "KILL":
                this._thisUser.isDead = true;
                this._dispatch(
                    setThisUserInfo({
                        ...this._thisUser
                    })
                );
                return {
                    senderId: SystemMessageType.YOU_WERE_KILLED,
                    message: strResource.notificationMessage.youWereKilled,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "INVESTIGATE":
                const data = await axios.get<CommonResponse<UserResponse>>(
                    REST_ONE_GAME_USER(this._gameId, result.skillTargetId)
                );
                const userResponse = data.data.data;
                return {
                    senderId: SystemMessageType.INVESTIGATION_RESULT,
                    message: `${userResponse.username}${
                        strResource.notificationMessage.heIs
                    } ${characterNameMap.get(
                        userResponse.characterCode as CharacterCode
                    )}${strResource.notificationMessage.wasHe}`,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "GUARD":
                return {
                    senderId: SystemMessageType.GUARD_SUCCESS,
                    message: strResource.notificationMessage.succeededToGuard,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "EXTERMINATE":
                this._thisUser.isDead = true;
                this._dispatch(setThisUserInfo({...this._thisUser}));
                return {
                    senderId: SystemMessageType.YOU_WERE_EXTERMINATED,
                    message:
                        strResource.notificationMessage.youWereExterminated,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "FAIL_TO_KILL":
                return {
                    senderId: SystemMessageType.SKILL_FAIL,
                    message: strResource.notificationMessage.failedToKill,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "FAIL_TO_INVESTIGATE":
                return {
                    senderId: SystemMessageType.SKILL_FAIL,
                    message:
                        strResource.notificationMessage.failedToInvestigate,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "FAIL_TO_GUARD":
                return {
                    senderId: SystemMessageType.SKILL_FAIL,
                    message: strResource.notificationMessage.failedToGuard,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "ENTER_WOLF_CHAT":
                console.log("ENTER_WOLF_CHAT");
                await participateChatContainer(this._gameId, ID_OF_WOLF_CHAT, NAME_OF_WOLF_CHAT, this._thisUser.userId)
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "NOTIFY":
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "NONE":
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            default:
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
        }
    }

    private processSkillEffectForOther(result: SkillResultResponse): Chat {
        let deadUser;
        switch (result.characterEffectAfterNightType) {
            case "KILL":
                for (let user of this._usersInRoom) {
                    if (user.userId === result.skillTargetId) {
                        deadUser = user;
                        user.isDead = true;
                    }
                }
                return {
                    senderId:
                        SYSTEM_MESSAGE_ID +
                        SystemMessageType.SOMEONE_WAS_KILLED,
                    message: `${deadUser?.username}${strResource.notificationMessage.someoneKilled}`,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "INVESTIGATE":
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "GUARD":
                return {
                    senderId: SystemMessageType.GUARD_SUCCESS,
                    message: strResource.notificationMessage.succeededToGuard,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "EXTERMINATE":
                for (let user of this._usersInRoom) {
                    if (user.userId === result.skillTargetId) {
                        deadUser = user;
                        user.isDead = true;
                    }
                }
                return {
                    senderId: SystemMessageType.SOMEONE_WAS_EXTERMINATED,
                    message:
                        strResource.notificationMessage.someoneExterminated,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "FAIL_TO_KILL":
                return {
                    senderId: SystemMessageType.SKILL_FAIL,
                    message: strResource.notificationMessage.failedToKill,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "FAIL_TO_INVESTIGATE":
                return {
                    senderId: SystemMessageType.SKILL_FAIL,
                    message:
                        strResource.notificationMessage.failedToInvestigate,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "FAIL_TO_GUARD":
                return {
                    senderId: SystemMessageType.SKILL_FAIL,
                    message: strResource.notificationMessage.failedToGuard,
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "ENTER_WOLF_CHAT":
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "NOTIFY":
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            case "NONE":
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
            default:
                return {
                    senderId: SystemMessageType.DUMMY,
                    message: "",
                    timestamp: new Date().getTime(),
                    containerId: ID_OF_PUBLIC_CHAT,
                    isMe: false
                };
        }
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
        const DEFAULT_COUNT = 90;
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
        this.startCountDown(howMany);
    }

    private startCountDown(count: number) {
        if (count <= 0) {
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

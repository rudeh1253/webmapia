import {ErrorCode} from "../error/ErrorCode";
import NotAssignedError from "../error/NotAssignedError";
import NullPointerError from "../error/NullPointerError";
import {setGameSwitch} from "../redux/slice/GameSwitchSlice";
import {setCurrentGamePhase} from "../redux/slice/currentGamePhaseSlice";
import {setGameConfiguration} from "../redux/slice/gameConfiguration";
import {setTimeCount} from "../redux/slice/timeCountSlice";
import SocketClient from "../sockjs/SocketClient";
import {GamePhase, GameSetting} from "../type/gameDomainType";
import {PhaseEndRequest, PostPhaseRequest} from "../type/requestType";
import {PhaseResultResponse} from "../type/responseType";
import {
    DEFAULT_TIME_CONFIGURATION,
    GAME_PHASE_ORDER,
    SOCKET_SEND_PHASE_END,
    SOCKET_SEND_POST_PHASE
} from "../util/const";

var sockClient: SocketClient;

export default class GameManager {
    private static singleton: GameManager;

    private _userId: number;
    private _gameId: number;
    private _gameSetting: GameSetting;
    private _currentGamePhase: GamePhase;
    private _dispatch: any;

    private constructor() {
        this._userId = 0;
        this._gameId = 0;
        this._gameSetting = DEFAULT_TIME_CONFIGURATION;
        this._currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
        this._dispatch = null;
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
        return this._userId;
    }

    public set userId(userId: number) {
        this._userId = userId;
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

    public async postPhase() {
        if (!sockClient) {
            sockClient = await SocketClient.getInstance();
        }
        if (this.gameId === 0) {
            throw new NotAssignedError(
                ErrorCode.GAME_ID_NOT_ASSIGNED_IN_GAME_MANAGER
            );
        }
        const body: PostPhaseRequest = {
            notificationType: "PHASE_RESULT",
            gameId: this._gameId,
            userId: this._userId
        };
        sockClient.sendMessage(SOCKET_SEND_POST_PHASE, {}, body);
    }

    public processPhaseResult(data: PhaseResultResponse) {
        console.log(data);
        if (data.gameEnd) {
            this._dispatch(setCurrentGamePhase(GamePhase.GAME_END));
        }
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
        console.log("GameConfig:", gameConfig);
        let howMany;
        switch (currentPhase) {
            case GamePhase.DAYTIME:
                howMany = gameConfig.discussionTimeSeconds;
                this._dispatch(setTimeCount(howMany));
                this.startCountDown(howMany);
                break;
            case GamePhase.VOTE:
                howMany = gameConfig.voteTimeSeconds;
                this._dispatch(setTimeCount(howMany));
                this.startCountDown(howMany);
                break;
            case GamePhase.NIGHT:
                howMany = gameConfig.nightTimeSeconds;
                this._dispatch(setTimeCount(howMany));
                this.startCountDown(howMany);
                break;
            case GamePhase.EXECUTION:
                break;
            default:
                const DEFAULT_COUNT = 90;
                this._dispatch(setTimeCount(DEFAULT_COUNT));
                this.startCountDown(DEFAULT_COUNT);
        }
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
        if (this._userId === 0) {
            throw new NotAssignedError(
                ErrorCode.USER_ID_NOT_ASSIGNED_IN_GAME_MANAGER
            );
        }
        const sockClient = await SocketClient.getInstance();
        const body: PhaseEndRequest = {
            notificationType: "PHASE_END",
            gameId: this._gameId,
            userId: this._userId
        };
        sockClient.sendMessage(SOCKET_SEND_PHASE_END, {}, body);
    }
}

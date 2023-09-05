import {ErrorCode} from "../error/ErrorCode";
import NotAssignedError from "../error/NotAssignedError";
import NullPointerError from "../error/NullPointerError";
import {setCurrentGamePhase} from "../redux/slice/currentGamePhaseSlice";
import {setTimeCount} from "../redux/slice/timeCountSlice";
import SocketClient from "../sockjs/SocketClient";
import {GamePhase, GameSetting} from "../type/gameDomainType";
import {PostPhaseRequest} from "../type/requestType";
import {
    DEFAULT_TIME_CONFIGURATION,
    GAME_PHASE_ORDER,
    SOCKET_SEND_POST_PHASE
} from "../util/const";

var sockClient: SocketClient;

export default class GameManager {
    private static singleton: GameManager;

    private _gameId: number;
    private _gameSetting: GameSetting;
    private _currentGamePhase: GamePhase;
    private _dispatch: any;

    private constructor() {
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
            gameId: this._gameId
        };
        sockClient.sendMessage(SOCKET_SEND_POST_PHASE, {}, body);
    }

    public moveToNextPhase() {
        if (this._dispatch === null) {
            throw new NullPointerError(
                ErrorCode.DISPATCH_IS_NULL_IN_GAME_MANAGER
            );
        }
        const nextPhase = this.getNextPhase(this._currentGamePhase);
        this._dispatch(setCurrentGamePhase(nextPhase));
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
            default:
                const DEFAULT_COUNT = 90;
                this._dispatch(setTimeCount(DEFAULT_COUNT));
                this.startCountDown(DEFAULT_COUNT);
        }
    }

    private startCountDown(count: number) {
        if (count <= 0) {
            return;
        }

        const SECOND_IN_MILLIS = 1000;
        setTimeout(() => {
            const c = count - 1;
            this._dispatch(setTimeCount(c));
            this.startCountDown(c);
        }, SECOND_IN_MILLIS);
    }
}

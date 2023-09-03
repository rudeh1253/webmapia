import SocketClient from "../sockjs/SocketClient";
import {
    GamePhase,
    GameSetting
} from "../type/gameDomainType";
import {SECOND_IN_MILLIS} from "../util/const";

export default class GameManager {
    private static singleton: GameManager;

    private _gameId: number;
    private _gameSetting: GameSetting | null;
    private _currentGamePhase: GamePhase;

    private constructor() {
        this._gameId = 0;
        this._gameSetting = null;
        this._currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
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

    public set currentGamePhase(gamePhase: GamePhase) {
        this._currentGamePhase = gamePhase;
    }

    public get currentGamePhase() {
        return this._currentGamePhase;
    }

    public set gameId(gameId: number) {
        this._gameId = gameId;
    }

    public startCountDown(
        callbackPerSecond: (time: number) => void,
        callbackAfterEnd: () => void
    ): void {
        let time: number;
        switch (this._currentGamePhase) {
            case GamePhase.DAYTIME:
                time = this._gameSetting!.discussionTimeSeconds;
                break;
            case GamePhase.NIGHT:
                time = this._gameSetting!.nightTimeSeconds;
                break;
            case GamePhase.VOTE:
                time = this._gameSetting!.voteTimeSeconds;
                break;
            default:
                time = 0;
        }
        if (!time) {
            time = 0;
        }

        const intervalId = setInterval(() => {
            time--;
            callbackPerSecond(time);
            if (time === 0) {
                clearInterval(intervalId);
                callbackAfterEnd();
            }
        }, SECOND_IN_MILLIS);
    }
}

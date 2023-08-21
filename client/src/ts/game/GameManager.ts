import {
    GamePhase,
    GameSetting
} from "../type/gameDomainType";
import {SECOND_IN_MILLIS} from "../util/const";

export default class GameManager {
    private static singleton: GameManager;

    private gameId: number;
    private gameSetting: GameSetting | null;
    private currentGamePhase: GamePhase;

    private constructor() {
        this.gameId = 0;
        this.gameSetting = null;
        this.currentGamePhase = null;
    }

    public static getInstance(): GameManager {
        if (this.singleton == null) {
            this.singleton = new GameManager();
        }
        return this.singleton;
    }

    public set setGameSetting(gameSetting: GameSetting) {
        this.gameSetting = gameSetting;
    }

    public set setCurrentGamePhase(gamePhase: GamePhase) {
        this.currentGamePhase = gamePhase;
    }

    public set setGameId(gameId: number) {
        this.gameId = gameId;
    }

    public startCountDown(
        callbackPerSecond: (time: number) => void,
        callbackAfterEnd: () => void
    ): void {
        let time: number;
        switch (this.currentGamePhase) {
            case "DAYTIME":
                time = this.gameSetting!.discussionTimeSeconds;
                break;
            case "NIGHT":
                time = this.gameSetting!.nightTimeSeconds;
                break;
            case "VOTE":
                time = this.gameSetting!.voteTimeSeconds;
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

import {
    GamePhase,
    GameSetting
} from "../type/gameDomainType";
import { GAME_PHASE_ORDER } from "./const";
import { setCurrentGamePhase } from "../redux/slice/currentGamePhaseSlice";
import { setTimeCount } from "../redux/slice/timeCountSlice";
import { gameManager } from "./getSubscription";

export function onPhaseEnd(
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

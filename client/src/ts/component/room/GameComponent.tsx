import {useState, useEffect} from "react";
import GameManager from "../../game/GameManager";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import {GamePhase} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import DistributionPhase from "./game/DistributionPhase";
import DaytimePhase from "./game/DaytimePhase";
import ExecutionPhase from "./game/ExecutionPhase";
import NightPhase from "./game/NightPhase";
import VotePhase from "./game/VotePhase";
import { characterNameMap } from "../../game/characterNameMap";
import EndPhase from "./game/EndPhase";

var gameManager: GameManager;

export default function GameComponent() {
    const [currentView, setCurrentView] = useState<JSX.Element>(<></>);

    const gameStarted = useAppSelector((state) => state.gameSwitch);
    const roomInfo = useAppSelector((state) => state.currentRoomInfo);
    const gameSetting = useAppSelector((state) => state.gameConfiugraion);
    const currentGamePhase = useAppSelector((state) => state.currentGamePhase);
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const timeCount = useAppSelector((state) => state.timeCount);

    const dispatch = useAppDispatch();

    const characterCodeOfUser = thisUser.characterCode as
        | "BETRAYER"
        | "CITIZEN"
        | "DETECTIVE"
        | "FOLLOWER"
        | "GUARD"
        | "HUMAN_MOUSE"
        | "MEDIUMSHIP"
        | "MURDERER"
        | "NOBILITY"
        | "PREDICTOR"
        | "SECRET_SOCIETY"
        | "SOLDIER"
        | "SUCCESSOR"
        | "TEMPLAR"
        | "WOLF";

    useEffect(() => {
        if (!gameManager) {
            gameManager = GameManager.getInstance();
            gameManager.dispatch = dispatch;
            gameManager.gameId = roomInfo.roomInfo.roomId;
        }
    }, []);

    useEffect(() => {
        console.log(currentGamePhase.value);
        switch (currentGamePhase.value) {
            case GamePhase.DAYTIME:
                setCurrentView(<DaytimePhase />);
                break;
            case GamePhase.CHARACTER_DISTRIBUTION:
                setCurrentView(<DistributionPhase />);
                break;
            case GamePhase.EXECUTION:
                setCurrentView(<ExecutionPhase />);
                break;
            case GamePhase.NIGHT:
                setCurrentView(<NightPhase />);
                break;
            case GamePhase.VOTE:
                setCurrentView(<VotePhase />);
                break;
            case GamePhase.GAME_END:
                setCurrentView(<EndPhase />);
                break;
            default:
                setCurrentView(<>{strResource.common.error}</>);
        }
    }, [currentGamePhase]);

    useEffect(() => {
        if (gameStarted) {
            gameManager.currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
            gameManager.gameId = roomInfo.roomInfo.roomId;
            gameManager.gameSetting = gameSetting;
        }
    }, [gameStarted]);

    return (
        <div className="game-container">
            <p className="character-p">
                {thisUser.characterCode === null
                    ? strResource.game.abscence
                    : characterNameMap.get(characterCodeOfUser)}
            </p>
            <p className="present-current-phase">{currentGamePhase.value}</p>
            <p className="time-counter">{timeCount}</p>
            {currentView}
        </div>
    );
}

import {useState, useEffect} from "react";
import GameManager from "../../game/GameManager";
import {useAppSelector} from "../../redux/hook";
import {GamePhase} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import DistributionPhase from "./game/DistributionPhase";
import DaytimePhase from "./game/DaytimePhase";
import ExecutionPhase from "./game/ExecutionPhase";
import NightPhase from "./game/NightPhase";
import VotePhase from "./game/VotePhase";
import {CHARACTER_NAME_MAP} from "../../util/const";

var gameManager: GameManager;

export default function GameComponent() {
    const [currentView, setCurrentView] = useState<JSX.Element>(<></>);

    const gameStarted = useAppSelector((state) => state.gameSwitch);
    const roomInfo = useAppSelector((state) => state.currentRoomInfo);
    const gameSetting = useAppSelector((state) => state.gameConfiugraion);
    const currentGamePhase = useAppSelector((state) => state.currentGamePhase);
    const thisUser = useAppSelector((state) => state.thisUserInfo);

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
        }
    }, []);

    useEffect(() => {
        switch (gameManager.currentGamePhase) {
            case GamePhase.BEFORE_START:
                setCurrentView(<></>);
                gameManager.currentGamePhase = GamePhase.BEFORE_START;
                break;
            case GamePhase.DAYTIME:
                setCurrentView(<DaytimePhase />);
                gameManager.currentGamePhase = GamePhase.DAYTIME;
                break;
            case GamePhase.CHARACTER_DISTRIBUTION:
                setCurrentView(<DistributionPhase />);
                gameManager.currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
                break;
            case GamePhase.EXECUTION:
                setCurrentView(<ExecutionPhase />);
                gameManager.currentGamePhase = GamePhase.EXECUTION;
                break;
            case GamePhase.NIGHT:
                setCurrentView(<NightPhase />);
                gameManager.currentGamePhase = GamePhase.NIGHT;
                break;
            case GamePhase.VOTE:
                setCurrentView(<VotePhase />);
                gameManager.currentGamePhase = GamePhase.VOTE;
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
                    : CHARACTER_NAME_MAP[characterCodeOfUser]}
            </p>
            {currentView}
        </div>
    );
}

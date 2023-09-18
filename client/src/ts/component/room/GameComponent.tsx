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
import {characterNameMap} from "../../game/characterNameMap";
import EndPhase from "./game/EndPhase";
import "../../../css/GameComponent.css";
import {phaseText} from "../../util/const";

var gameManager: GameManager;

export default function GameComponent() {
    const [currentView, setCurrentView] = useState<JSX.Element>(<></>);
    const [characterShownColor, setCharacterShownColor] =
        useState<string>("#343a40");
    const [pressedPhaseEnd, setPressedPhaseEnd] = useState<boolean>(false);

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
        switch (characterCodeOfUser) {
            case "WOLF":
            case "BETRAYER":
            case "FOLLOWER":
                setCharacterShownColor("#e03131");
                break;
            case "CITIZEN":
            case "DETECTIVE":
            case "GUARD":
            case "MEDIUMSHIP":
            case "MURDERER":
            case "NOBILITY":
            case "PREDICTOR":
            case "SECRET_SOCIETY":
            case "SOLDIER":
            case "SUCCESSOR":
            case "TEMPLAR":
                setCharacterShownColor("#343a40");
                break;
            case "HUMAN_MOUSE":
                setCharacterShownColor("#2f9e44");
                break;
            default:
                setCharacterShownColor("#343a40");
        }
    }, [thisUser]);

    useEffect(() => {
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
        setPressedPhaseEnd(false);
    }, [currentGamePhase]);

    useEffect(() => {
        if (gameStarted) {
            gameManager.currentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
            gameManager.gameId = roomInfo.roomInfo.roomId;
            gameManager.gameSetting = gameSetting;
        }
    }, [gameStarted]);

    return (
        <div className="game-component-container">
            {thisUser.isDead ? null : (
                <button
                    className="btn--phase-end"
                    type="button"
                    onClick={() => {
                        gameManager.manualEnd();
                        setPressedPhaseEnd(true);
                    }}
                    disabled={pressedPhaseEnd}
                >
                    Phase end
                </button>
            )}
            <div className="util-container">
                <p className="character-p" style={{color: characterShownColor}}>
                    {thisUser.characterCode === null
                        ? strResource.game.abscence
                        : characterNameMap.get(characterCodeOfUser)}
                </p>
                <p className="present-current-phase">
                    {phaseText[currentGamePhase.value]}
                </p>
                <p className="time-counter">{timeCount}</p>
            </div>
            {currentView}
        </div>
    );
}

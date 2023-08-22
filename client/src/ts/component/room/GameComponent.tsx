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

var gameManager: GameManager;

export default function GameComponent() {
    const [currentView, setCurrentView] = useState<JSX.Element>(<></>);

    const gameStarted = useAppSelector((state) => state.gameSwitch);
    const roomInfo = useAppSelector((state) => state.currentRoomInfo);
    const gameSetting = useAppSelector((state) => state.gameConfiugraion);
    const currentGamePhase = useAppSelector((state) => state.currentGamePhase);

    useEffect(() => {
        if (!gameManager) {
            gameManager = GameManager.getInstance();
        }
    }, []);

    useEffect(() => {
        switch (currentGamePhase.value) {
            case GamePhase.BEFORE_START:
                setCurrentView(<></>);
                break;
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
    return <div>{currentView}</div>;
}

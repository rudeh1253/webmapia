import {useEffect} from "react";
import GameManager from "../../game/GameManager";
import {useAppSelector} from "../../redux/hook";
import {GamePhase} from "../../type/gameDomainType";

var gameManager: GameManager;

export default function GameComponent() {
    const gameStarted = useAppSelector((state) => state.gameSwitch);
    const roomInfo = useAppSelector((state) => state.currentRoomInfo);
    const gameSetting = useAppSelector((state) => state.gameConfiugraion);
    useEffect(() => {
        if (!gameManager) {
            gameManager = GameManager.getInstance();
        }
    }, []);

    useEffect(() => {
        if (gameStarted) {
            gameManager.setCurrentGamePhase = GamePhase.CHARACTER_DISTRIBUTION;
            gameManager.setGameId = roomInfo.roomInfo.roomId;
            gameManager.setGameSetting = gameSetting;
        }
    }, [gameStarted]);
    return <></>;
}

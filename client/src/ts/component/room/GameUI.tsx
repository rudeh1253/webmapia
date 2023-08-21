import {useEffect} from "react";
import GameManager from "../../game/GameManager";
import {useAppSelector} from "../../redux/hook";

var gameManager: GameManager;

export default function GameUI() {
    const gameStarted = useAppSelector((state) => state.gameSwitch);
    useEffect(() => {
        if (!gameManager) {
            gameManager = GameManager.getInstance();
        }
    }, []);

    useEffect(() => {
        // TODO: game started
    }, [gameStarted]);
    return <></>;
}

import {useEffect} from "react";
import strResource from "../../../../resource/string.json";
import {useAppSelector} from "../../../redux/hook";
import {GamePhase} from "../../../type/gameDomainType";
import SocketClient from "../../../sockjs/SocketClient";
import GameManager from "../../../game/GameManager";

var sockClient: SocketClient;
var gameManager: GameManager;

export default function DistributionPhase() {
    const gamePhase = useAppSelector((state) => state.currentGamePhase);
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const roomInfo = useAppSelector((state) => state.currentRoomInfo);

    useEffect(() => {
        if (!sockClient) {
            SocketClient.getInstance().then((result) => (sockClient = result));
        }
        if (!gameManager) {
            gameManager = GameManager.getInstance();
        }
    }, []);

    useEffect(() => {
        if (
            gamePhase.value === GamePhase.CHARACTER_DISTRIBUTION &&
            thisUser.userId === roomInfo.roomInfo.hostId
        ) {
            gameManager.distributeCharacters();
        }
    }, [gamePhase]);
    return (
        <div className="game-container distribution-phase">
            <p className="distribution-message">
                {strResource.game.inDistribution}
            </p>
        </div>
    );
}

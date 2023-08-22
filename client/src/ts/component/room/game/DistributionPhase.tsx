import {useEffect} from "react";
import strResource from "../../../../resource/string.json";
import {useAppSelector} from "../../../redux/hook";
import {GamePhase} from "../../../type/gameDomainType";
import SocketClient from "../../../sockjs/SocketClient";
import GameManager from "../../../game/GameManager";
import {Subscription} from "stompjs";
import {SOCKET_SUBSCRIBE_NOTIFICATION_PRIVATE} from "../../../util/const";

var sockClient: SocketClient;
var gameManager: GameManager;
var distributionSubscription: Subscription;

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
        if (!distributionSubscription) {
            sockClient.subscribe(
                SOCKET_SUBSCRIBE_NOTIFICATION_PRIVATE(roomInfo.roomInfo.roomId, thisUser.userId),
                (payload) => {}
            );
        }
    });

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

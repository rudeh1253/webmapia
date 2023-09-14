import {useState, useEffect} from "react";
import strResource from "../../../../resource/string.json";
import {useAppSelector} from "../../../redux/hook";
import {UserInfo} from "../../../type/gameDomainType";
import SocketClient from "../../../sockjs/SocketClient";
import {SOCKET_SEND_VOTE} from "../../../util/const";
import {VoteRequest} from "../../../type/requestType";
import GameManager from "../../../game/GameManager";

var sockClient: SocketClient;

export default function VotePhase() {
    const [selectedUser, setSelectedUser] = useState<UserInfo>();
    const userIdsInRoom = useAppSelector((state) => state.userIdsInRoom);
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const roomInfo = useAppSelector((state) => state.currentRoomInfo).roomInfo;

    const gameManager = GameManager.getInstance();

    useEffect(() => {
        if (!sockClient) {
            SocketClient.getInstance().then(
                (instance) => (sockClient = instance)
            );
        }
    }, []);

    return (
        <div className="phase-container vote-phase">
            <p>{strResource.game.vote}</p>
            {userIdsInRoom.map((id) => {
                const user = gameManager.getUser(id);
                return user!.isDead ? null : (
                    <button type="button" onClick={() => setSelectedUser(user)}>
                        {user!.username}
                    </button>
                );
            })}
            <button
                type="button"
                onClick={() => {
                    if (sockClient) {
                        const body: VoteRequest = {
                            gameId: roomInfo.roomId,
                            voterId: thisUser.userId,
                            subjectId: selectedUser!.userId
                        };
                        sockClient.sendMessage(SOCKET_SEND_VOTE, {}, body);
                    }
                }}
                disabled={!selectedUser}
            >
                {strResource.game.vote}
            </button>
        </div>
    );
}

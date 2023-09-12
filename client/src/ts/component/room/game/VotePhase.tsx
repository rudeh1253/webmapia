import {useState, useEffect} from "react";
import strResource from "../../../../resource/string.json";
import {useAppSelector} from "../../../redux/hook";
import {UserInfo} from "../../../type/gameDomainType";
import SocketClient from "../../../sockjs/SocketClient";
import {SOCKET_SEND_VOTE} from "../../../util/const";
import {VoteRequest} from "../../../type/requestType";

var sockClient: SocketClient;

export default function VotePhase() {
    const [selectedUser, setSelectedUser] = useState<UserInfo>();
    const usersInRoom = useAppSelector((state) => state.usersInRoom);
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const roomInfo = useAppSelector((state) => state.currentRoomInfo).roomInfo;

    useEffect(() => {
        if (!sockClient) {
            SocketClient.getInstance().then(
                (instance) => (sockClient = instance)
            );
        }
    }, []);

    return (
        <div className="game-container vote-phase">
            <p>{strResource.game.vote}</p>
            {usersInRoom.map((user) =>
                user.isDead ? null : (
                    <button type="button" onClick={() => setSelectedUser(user)}>
                        {user.username}
                    </button>
                )
            )}
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

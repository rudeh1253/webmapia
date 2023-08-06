import {useRef, useState, useEffect} from "react";
import {Chat, RoomInfo, UserInfo} from "../type/gameDomainType";
import data from "../../resource/string.json";
import {useAppSelector} from "../redux/hook";

type RoomDetail = RoomInfo & {
    users: UserInfo[];
};

export default function Room() {
    const [users, setUsers] = useState<Array<UserInfo>>([]);
    const [chatLogs, setChatLogs] = useState<Array<Chat>>([]);
    const [roomDetail, setRoomDetail] = useState<RoomDetail>();

    const currentRoomInfo = useAppSelector((state) => state.currentRoomInfo);

    const chatInputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        // TODO: Fetch user info belonging to this room from server
        // axios.get(...)
        // For now, use temporary data
        const tempUsers: UserInfo[] = [
            {
                userId: 1,
                username: "Michael",
                characterCode: "BETRAYER",
                isDead: false
            },
            {
                userId: 2,
                username: "Michael",
                characterCode: "WOLF",
                isDead: false
            },
            {
                userId: 3,
                username: "Annie",
                characterCode: "GUARD",
                isDead: false
            },
            {
                userId: 4,
                username: "Kenny",
                characterCode: "MEDIUMSHIP",
                isDead: false
            },
            {
                userId: 5,
                username: "Router",
                characterCode: "SOLDIER",
                isDead: false
            }
        ];
        setRoomDetail({
            ...currentRoomInfo.roomInfo,
            users: tempUsers
        });
    }, [currentRoomInfo.roomInfo]);

    return (
        <div className="room-container">
            <ul className="user-list">
                {users.map((user, idx) => (
                    <li key={`user-item-${idx}`}>
                        <UserItem
                            userId={user.userId}
                            username={user.username}
                            characterCode={user.characterCode}
                            isDead={user.isDead}
                        />
                    </li>
                ))}
            </ul>
            <div className="chat-container">
                <div className="chat-log">
                    {chatLogs.map((chat, idx) => (
                        <ChatItem
                            key={`chat-item-${idx + 1}`}
                            sender={chat.sender}
                            message={chat.message}
                            timestamp={chat.timestamp}
                            isMe={chat.isMe}
                        />
                    ))}
                </div>
                <div className="enter-chat">
                    <div className="message-input-container">
                        <input
                            className="message-input"
                            type="text"
                            ref={chatInputRef}
                        />
                        <button className="send-message" type="button">
                            {data.room.send}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

function UserItem({userId, username, characterCode, isDead}: UserInfo) {
    return (
        <div>
            <p>{userId}</p>
            <p>{username}</p>
            <p>{characterCode}</p>
            <p>{isDead}</p>
        </div>
    );
}

function ChatItem({sender, message, timestamp, isMe}: Chat) {
    return (
        <div>
            <p>
                <UserItem
                    userId={sender.userId}
                    username={sender.username}
                    characterCode={sender.characterCode}
                    isDead={sender.isDead}
                />
            </p>
            <p>{message}</p>
            <p>{timestamp.toString()}</p>
        </div>
    );
}

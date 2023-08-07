import {useRef, useState, useEffect} from "react";
import {Chat, UserInfo} from "../type/gameDomainType";
import data from "../../resource/string.json";
import {useAppSelector} from "../redux/hook";

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

export default function Room() {
    const [thisUser, setThisUser] = useState<UserInfo>({
        userId: -1,
        username: "temp",
        characterCode: null,
        isDead: false
    });
    const [users, setUsers] = useState<Array<UserInfo>>([]);
    const [chatLogs, setChatLogs] = useState<Array<Chat>>([]);

    const currentRoomInfo = useAppSelector((state) => state.currentRoomInfo);

    const chatInputRef = useRef<HTMLInputElement>(null);

    const onUserEnter = (newUser: UserInfo) => setUsers([...users, newUser]);

    const onChatReceived = (newChat: Chat) =>
        setChatLogs([...chatLogs, newChat]);

    // TODO: Remove temporary data after testing
    const tempChatLog: Chat[] = [
        {
            sender: tempUsers[0],
            message: "Hi",
            timestamp: new Date(2023, 7, 7, 2, 30, 0).getTime(),
            isMe: false
        },
        {
            sender: tempUsers[1],
            message: "Hello",
            timestamp: new Date(2023, 7, 7, 2, 32, 0).getTime(),
            isMe: false
        },
        {
            sender: tempUsers[0],
            message: "How are you",
            timestamp: new Date(2023, 7, 7, 2, 32, 23).getTime(),
            isMe: false
        },
        {
            sender: tempUsers[3],
            message: "Come on",
            timestamp: new Date(2023, 7, 7, 2, 32, 50).getTime(),
            isMe: false
        }
    ];

    useEffect(() => {
        setChatLogs(tempChatLog);
        console.log(tempChatLog);
    }, []);

    const chat = (message: string) => {
        const newChat: Chat = {
            sender: thisUser,
            message,
            timestamp: Date.now(),
            isMe: true
        };
        tempChat(newChat); // Temporary chat
        // TODO: send newChat
    };

    const tempChat = (newChat: Chat) => {
        setChatLogs([...chatLogs, newChat]);
    };

    useEffect(() => {
        // TODO: Fetch user info belonging to this room from server
        // axios.get(...)
        // For now, use temporary data
        setUsers(tempUsers);
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
                        <button
                            className="send-message"
                            type="button"
                            onClick={() => chat(chatInputRef.current!.value)}
                        >
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
            <p>{isDead.toString()}</p>
        </div>
    );
}

function ChatItem({sender, message, timestamp, isMe}: Chat) {
    const time = new Date(timestamp);
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
            <p>{`${time.getHours()}:${time.getMinutes()}:${time.getSeconds()}/${time.getFullYear()}-${time.getMonth()}-${time.getDay()}`}</p>
        </div>
    );
}

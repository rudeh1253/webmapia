import { useRef, useState } from "react";
import { Chat, UserInfo } from "../type/gameDomainType";
import data from "../../resource/string.json";

export default function Room() {
    const [users, setUsers] = useState<Array<UserInfo>>([]);
    const [chatLogs, setChatLogs] = useState<Array<Chat>>([]);

    const chatInputRef = useRef<HTMLInputElement>(null);

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
                    {chatLogs.map((chat) => (
                        <ChatItem
                            sender={chat.sender}
                            message={chat.message}
                            timestamp={chat.timestamp}
                        />
                    ))}
                </div>
                <div className="enter-chat">
                    <div className="message-input-container">
                        <input className="message-input" type="text" ref={chatInputRef} />
                        <button className="send-message" type="button">{data.room.send}</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

function UserItem({ userId, username, characterCode, isDead }: UserInfo) {
    return (
        <div>
            <p>{userId}</p>
            <p>{username}</p>
            <p>{characterCode}</p>
            <p>{isDead}</p>
        </div>
    );
}

function ChatItem({ sender, message, timestamp }: Chat) {
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

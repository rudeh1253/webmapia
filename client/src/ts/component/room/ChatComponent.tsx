import {useState, useRef} from "react";
import {ChatStorage} from "../../type/gameDomainType";
import {iChatStorage} from "../../util/initialState";
import {ChatItem} from "./RoomSubcomponent";
import strResource from "../../../resource/string.json";

export default function ChatComponent() {
    const [currentChatStorage, setCurrentChatStorage] =
        useState<ChatStorage>(iChatStorage);
    const [chatStorages, setChatStorages] = useState<ChatStorage[]>([]);
    const chatInputRef = useRef<HTMLInputElement>(null);
    return (
        <div className="chat-container">
            <div className="tab-container">
                {chatStorages.map((storage) => (
                    <button
                        className="tab"
                        type="button"
                        onClick={() => setCurrentChatStorage(storage)}
                    >
                        {storage.name}
                    </button>
                ))}
            </div>
            <div className="chat-log">
                {currentChatStorage.chatLogs.map((chat, idx) => (
                    <ChatItem
                        key={`chat-item-${idx + 1}`}
                        senderId={chat.senderId}
                        message={chat.message}
                        timestamp={chat.timestamp}
                        isPublic={chat.isPublic}
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
                        onClick={() => undefined}
                    >
                        {strResource.room.send}
                    </button>
                </div>
            </div>
        </div>
    );
}

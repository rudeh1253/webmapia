import {useState, useRef, useEffect} from "react";
import {
    Chat,
    ChatStorage,
    GamePhase,
    UserInfo
} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import InvalidArgumentError from "../../error/InvalidArgumentError";
import {ErrorCode} from "../../error/ErrorCode";
import {ID_OF_PUBLIC_CHAT} from "../../util/const";
import {iChatStorage} from "../../util/initialState";
import {sendPrivateChat, sendPublicChat} from "../../util/chat";

export type ChatComponentProp = {
    users: UserInfo[];
};

export default function ChatComponent({users}: ChatComponentProp) {
    const [currentChatStorage, setCurrentChatStorage] =
        useState<ChatStorage>(iChatStorage);
    const [chatStorages, setChatStorages] = useState<ChatStorage[]>([]);

    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const currentRoomInfo = useAppSelector((state) => state.currentRoomInfo);
    const currentGamePhase = useAppSelector(
        (state) => state.currentGamePhase
    ).value;
    const newChat = useAppSelector((state) => state.newChat);

    const chatInputRef = useRef<HTMLInputElement>(null);

    const dispatch = useAppDispatch();

    const resetChatStorage = () => {
        const a: ChatStorage[] = [
            {
                id: ID_OF_PUBLIC_CHAT,
                participants: users,
                name: strResource.game.publicChat,
                chatLogs: []
            }
        ];
        setChatStorages(a);
        setCurrentChatStorage(a[0]);
    };

    useEffect(() => {
        if (chatStorages.length === 0) {
            resetChatStorage();
        }
    }, []);

    useEffect(() => {
        if (currentGamePhase === GamePhase.GAME_END) {
            resetChatStorage();
        }
    }, [currentGamePhase]);

    useEffect(() => {
        let chatStorage;
        for (let i of chatStorages) {
            if (i.id === newChat.containerId) {
                chatStorage = i;
            }
        }
        if (chatStorage) {
            const chatLogs = chatStorage.chatLogs;
            if (chatLogs.length === 0 || newChat.timestamp !== chatLogs[chatLogs.length - 1].timestamp) {
                chatLogs.push(newChat);
                setChatStorages([...chatStorages]);
            }
        }
    }, [newChat]);

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
                        containerId={chat.containerId}
                        isMe={chat.isMe}
                    />
                ))}
            </div>
            <div className="enter-chat-message">
                <div className="message-input-container">
                    <input
                        className="message-input"
                        type="text"
                        ref={chatInputRef}
                    />
                    <button
                        className="send-message"
                        type="button"
                        onClick={() =>
                            currentChatStorage.id === ID_OF_PUBLIC_CHAT
                                ? sendPublicChat(
                                      chatInputRef.current!.value,
                                      currentRoomInfo,
                                      thisUser
                                  )
                                : sendPrivateChat(
                                      chatInputRef.current!.value,
                                      currentRoomInfo,
                                      thisUser,
                                      currentChatStorage.id
                                  )
                        }
                    >
                        {strResource.room.send}
                    </button>
                </div>
            </div>
        </div>
    );
}

function ChatItem({senderId, message, timestamp, containerId, isMe}: Chat) {
    const time = new Date(timestamp);
    return (
        <div>
            <p>{senderId}</p>
            <p>{message}</p>
            <p>{`${time.getHours()}:${time.getMinutes()}:${time.getSeconds()}/${time.getFullYear()}-${time.getMonth()}-${time.getDay()}`}</p>
        </div>
    );
}

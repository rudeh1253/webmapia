import {useState, useRef, useEffect} from "react";
import {
    Chat,
    ChatContainer,
    ChatContainerTab,
    GamePhase,
    UserInfo
} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import {ID_OF_PUBLIC_CHAT} from "../../util/const";
import {iChatStorage} from "../../util/initialState";
import {sendPrivateChat, sendPublicChat} from "../../sockjs/chat";

export type ChatComponentProp = {
    users: UserInfo[];
};

const chatContainerMap = new Map<number, ChatContainer>();

export default function ChatComponent({users}: ChatComponentProp) {
    const [currentChatContainer, setCurrentChatContainer] =
        useState<ChatContainer>(iChatStorage);
    const [chatContainerTabs, setChatContainerTabs] = useState<
        ChatContainerTab[]
    >([]);

    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const currentRoomInfo = useAppSelector((state) => state.currentRoomInfo);
    const currentGamePhase = useAppSelector(
        (state) => state.currentGamePhase
    ).value;
    const newChat = useAppSelector((state) => state.newChat);
    const newChatContainer = useAppSelector((state) => state.newChatContainer);

    const chatInputRef = useRef<HTMLInputElement>(null);

    const dispatch = useAppDispatch();

    const resetChatStorage = () => {
        const initialElement: ChatContainer = {
            id: ID_OF_PUBLIC_CHAT,
            participants: users,
            name: strResource.game.publicChat,
            chatLogs: []
        };
        chatContainerMap.set(initialElement.id, initialElement);
        setChatContainerTabs(extractKeysAndNames(chatContainerMap));
        const publicChatContainer = chatContainerMap.get(ID_OF_PUBLIC_CHAT);
        if (publicChatContainer) {
            setCurrentChatContainer(publicChatContainer);
        }
    };

    useEffect(() => {
        if (chatContainerMap.size === 0) {
            resetChatStorage();
        }
    }, []);

    useEffect(() => {
        if (currentGamePhase === GamePhase.GAME_END) {
            resetChatStorage();
        }
    }, [currentGamePhase]);

    useEffect(() => {
        const chatContainer = chatContainerMap.get(newChat.containerId);
        if (chatContainer) {
            if (
                chatContainer.chatLogs.length > 0 &&
                chatContainer.chatLogs[chatContainer.chatLogs.length - 1]
                    .timestamp === newChat.timestamp
            ) {
                return;
            }
            const origChatLogs = chatContainer.chatLogs;
            chatContainer.chatLogs = [...origChatLogs, newChat];
            setChatContainerTabs(extractKeysAndNames(chatContainerMap));
            if (chatContainer.id === currentChatContainer.id) {
                setCurrentChatContainer({...chatContainer});
            }
        }
    }, [newChat]);

    useEffect(() => {
        if (
            newChatContainer.id !== -1 &&
            !chatContainerMap.has(newChatContainer.id)
        ) {
            const participantUsers: UserInfo[] = [];
            for (let id of newChatContainer.participants) {
                for (let user of users) {
                    if (id.userId === user.userId) {
                        participantUsers.push(user);
                    }
                }
            }
            chatContainerMap.set(newChatContainer.id, {
                ...newChatContainer,
                participants: participantUsers
            });
            setChatContainerTabs(extractKeysAndNames(chatContainerMap));
        }
    }, [newChatContainer]);

    return (
        <div className="chat-container">
            <div className="tab-container">
                {chatContainerTabs.map((e) => {
                    return (
                        <button
                            className="tab"
                            type="button"
                            onClick={() => {
                                setCurrentChatContainer({
                                    ...chatContainerMap.get(e.key)!
                                });
                            }}
                        >
                            {e.name}
                        </button>
                    );
                })}
            </div>
            <div className="chat-log">
                {currentChatContainer.chatLogs.map((chat, idx) => (
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
                            currentChatContainer.id === ID_OF_PUBLIC_CHAT
                                ? sendPublicChat(
                                      chatInputRef.current!.value,
                                      currentRoomInfo,
                                      thisUser
                                  )
                                : sendPrivateChat(
                                      chatInputRef.current!.value,
                                      currentRoomInfo,
                                      thisUser,
                                      currentChatContainer.id
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

function extractKeysAndNames(map: Map<number, ChatContainer>) {
    const tabInfos: ChatContainerTab[] = [];
    const keyIterator = map.keys();
    while (true) {
        const k = keyIterator.next();
        if (k.done) {
            break;
        }
        tabInfos.push({
            key: k.value,
            name: map.has(k.value) ? map.get(k.value)!.name : ""
        });
    }
    return tabInfos;
}

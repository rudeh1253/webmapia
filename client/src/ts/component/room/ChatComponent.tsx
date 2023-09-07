import {useState, useRef, useEffect} from "react";
import {
    Chat,
    ChatContainer,
    GamePhase,
    UserInfo
} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import {ID_OF_PUBLIC_CHAT} from "../../util/const";
import {iChatStorage} from "../../util/initialState";
import {sendPrivateChat, sendPublicChat} from "../../util/chat";

export type ChatComponentProp = {
    users: UserInfo[];
};

export default function ChatComponent({users}: ChatComponentProp) {
    const [currentChatContainer, setCurrentChatContainer] =
        useState<ChatContainer>(iChatStorage);
    const [chatContainerMap, setChatContainerMap] = useState<
        Map<number, ChatContainer>
    >(new Map<number, ChatContainer>());
    const [chatContainerMapKeys, setChatContainerMapKeys] = useState<number[]>(
        []
    );

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
        const map = new Map<number, ChatContainer>();
        map.set(initialElement.id, initialElement);
        setChatContainerMap(map);
        setChatContainerMapKeys(getKeys(map));
        const publicChatContainer = map.get(ID_OF_PUBLIC_CHAT);
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
            chatContainerMap.delete(chatContainer.id);
            const chatLogs = [...chatContainer.chatLogs, newChat];
            const ncc = {
                ...chatContainer,
                chatLogs
            };
            chatContainerMap.set(ncc.id, ncc);

            const newChatContainerMap = new Map<number, ChatContainer>(
                chatContainerMap
            );
            setChatContainerMap(newChatContainerMap);
            setChatContainerMapKeys(getKeys(newChatContainerMap));
            if (ncc.id === currentChatContainer.id) {
                setCurrentChatContainer(ncc);
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
            const newChatContainerMap = new Map<number, ChatContainer>(
                chatContainerMap
            );
            const cc: ChatContainer = {
                ...newChatContainer,
                participants: participantUsers
            };
            newChatContainerMap.set(cc.id, cc);
            setChatContainerMap(newChatContainerMap);
            setChatContainerMapKeys(getKeys(newChatContainerMap));
        }
    }, [newChatContainer]);

    return (
        <div className="chat-container">
            <div className="tab-container">
                {chatContainerMapKeys.map((key) => {
                    return (
                        <button
                            className="tab"
                            type="button"
                            onClick={() => {
                                setCurrentChatContainer({
                                    ...chatContainerMap.get(key)!
                                });
                            }}
                        >
                            {chatContainerMap.get(key)?.name}
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

function getKeys(map: Map<any, any>) {
    const keys = [];
    const keyIterator = map.keys();
    while (true) {
        const k = keyIterator.next();
        if (k.done) {
            break;
        }
        keys.push(k.value);
    }
    return keys;
}

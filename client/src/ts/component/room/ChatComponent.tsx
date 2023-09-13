import {useState, useRef, useEffect} from "react";
import {
    Chat,
    ChatContainer,
    ChatContainerTab,
    GamePhase
} from "../../type/gameDomainType";
import strResource from "../../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import {
    ID_OF_CHAT_FOR_DEAD,
    ID_OF_PUBLIC_CHAT,
    SystemMessengerId,
    systemMessageTypeMap
} from "../../util/const";
import {iChatStorage} from "../../util/initialState";
import {sendPrivateChat, sendPublicChat} from "../../sockjs/chat";
import GameManager from "../../game/GameManager";

export type ChatComponentProp = {
    userIds: number[];
};

export var chatContainerMap = new Map<number, ChatContainer>();

var messageSender: {
    chatContainerId: number;
    sendMessage: (msg: string) => void;
} = {
    chatContainerId: -1,
    sendMessage: (msg: string) => {}
};

export default function ChatComponent({userIds}: ChatComponentProp) {
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
            participants: userIds,
            name: strResource.game.publicChat,
            chatLogs: []
        };
        chatContainerMap.set(initialElement.id, initialElement);
        setChatContainerTabs(extractKeysAndNames(chatContainerMap));
        const publicChatContainer = chatContainerMap.get(ID_OF_PUBLIC_CHAT);
        if (publicChatContainer) {
            setCurrentChatContainer(publicChatContainer);
            messageSender.chatContainerId = publicChatContainer.id;
        }
    };

    messageSender.sendMessage = (msg: string) => {
        messageSender.chatContainerId === ID_OF_PUBLIC_CHAT
            ? sendPublicChat(msg, currentRoomInfo, thisUser)
            : sendPrivateChat(
                  msg,
                  currentRoomInfo,
                  thisUser,
                  messageSender.chatContainerId
              );
    };

    const keyEventListener = (ev: KeyboardEvent) => {
        if (ev.key === "Enter") {
            messageSender.sendMessage(chatInputRef.current!.value);
            chatInputRef.current!.value = "";
        }
    };

    useEffect(() => {
        if (chatContainerMap.size === 0) {
            resetChatStorage();
        }
        if (chatInputRef.current) {
            document.addEventListener("keydown", keyEventListener);
        }
        return () => {
            setCurrentChatContainer(iChatStorage);
            messageSender.chatContainerId = iChatStorage.id;
            setChatContainerTabs([]);
            chatContainerMap.clear();
            document.removeEventListener("keydown", keyEventListener, true);
        };
    }, []);

    useEffect(() => {
        if (currentGamePhase === GamePhase.GAME_END) {
            resetChatStorage();
        }
    }, [currentGamePhase]);

    useEffect(() => {
        newChat.forEach((c) => {
            const chatContainer = chatContainerMap.get(c.containerId);
            if (chatContainer) {
                if (
                    chatContainer.chatLogs.length > 0 &&
                    chatContainer.chatLogs[0].timestamp === c.timestamp
                ) {
                    return;
                }
                const newChatContainer: ChatContainer = {
                    ...chatContainer,
                    chatLogs: [c, ...chatContainer.chatLogs]
                };
                chatContainerMap.set(chatContainer.id, newChatContainer);
                if (chatContainer.id === currentChatContainer.id) {
                    setCurrentChatContainer(newChatContainer);
                    messageSender.chatContainerId = newChatContainer.id;
                }
            }
        });
    }, [newChat]);

    useEffect(() => {
        if (newChatContainer.id !== -1) {
            if (!chatContainerMap.has(newChatContainer.id)) {
                chatContainerMap.set(newChatContainer.id, {
                    ...newChatContainer
                });
                setChatContainerTabs(extractKeysAndNames(chatContainerMap));
            } else {
                const previousChatContainer = chatContainerMap.get(
                    newChatContainer.id
                );
                if (
                    newChatContainer.participants.length !==
                    previousChatContainer!.participants.length
                ) {
                    chatContainerMap.set(newChatContainer.id, newChatContainer);
                    if (newChatContainer.id === currentChatContainer.id) {
                        setCurrentChatContainer(newChatContainer);
                        messageSender.chatContainerId = newChatContainer.id;
                    }
                }
            }
        }
    }, [newChatContainer]);

    return (
        <div className="chat-container">
            <div className="tab-container">
                {chatContainerTabs.map((e) => {
                    const classNameForBtn =
                        "tab" +
                        (currentChatContainer.id === e.key
                            ? " tab-current"
                            : "");
                    return (
                        <button
                            className={classNameForBtn}
                            type="button"
                            onClick={() => {
                                const container = chatContainerMap.get(e.key);
                                if (container) {
                                    setCurrentChatContainer({...container});
                                    messageSender.chatContainerId =
                                        container.id;
                                }
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
            <div className="message-input-container">
                <input
                    className="message-input"
                    id="message-in"
                    type="text"
                    ref={chatInputRef}
                />
                <button
                    className="btn--send-message"
                    type="button"
                    onClick={() => {
                        messageSender.sendMessage(chatInputRef.current!.value);
                        chatInputRef.current!.value = "";
                    }}
                    disabled={
                        thisUser.isDead &&
                        currentChatContainer.id !== ID_OF_CHAT_FOR_DEAD
                    }
                >
                    {strResource.room.send}
                </button>
            </div>
        </div>
    );
}

function ChatItem({senderId, message, timestamp, containerId, isMe}: Chat) {
    const gameManager = GameManager.getInstance();
    const time = new Date(timestamp);
    const ts = `${time.getHours()}:${time.getMinutes()}:${time.getSeconds()}/${time.getFullYear()}-${time.getMonth()}-${time.getDay()}`;

    const systemMessageClass =
        senderId < 0
            ? `system-message ${systemMessageTypeMap.get(
                  senderId as SystemMessengerId
              )?.className}`
            : "";

    const chatItemClass = "chat-item" + (isMe ? " my-message" : "");
    return (
        <div className="chat-item-wrapper">
            <p>{senderId}</p>
            {senderId < 0 ? (
                <div className={systemMessageClass}>
                    <p className="message">{message}</p>
                    <p className="timestamp">{ts}</p>
                </div>
            ) : (
                <div className={chatItemClass}>
                    <p className="sender">
                        {gameManager.getUser(senderId)!.username}
                    </p>
                    <p className="message">{message}</p>
                    <p className="sended-time">{ts}</p>
                </div>
            )}
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

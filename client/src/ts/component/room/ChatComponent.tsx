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
    ID_OF_CLEAR_CHAT_CONTAINER,
    ID_OF_PUBLIC_CHAT,
    MESSAGE_SEPEARTION_ID,
    SystemMessengerId,
    systemMessageTypeMap
} from "../../util/const";
import {iChatStorage} from "../../util/initialState";
import {sendPrivateChat, sendPublicChat} from "../../sockjs/chat";
import GameManager from "../../game/GameManager";

export type ChatComponentProp = {
    userIds: number[];
    inited: boolean;
};

export var chatContainerMap = new Map<number, ChatContainer>();

var messageSender: {
    chatContainerId: number;
    sendMessage: (msg: string) => void;
} = {
    chatContainerId: -1,
    sendMessage: (msg: string) => {}
};

export default function ChatComponent({userIds, inited}: ChatComponentProp) {
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
        if (inited) {
            if (thisUser.isDead) {
                if (messageSender.chatContainerId === ID_OF_CHAT_FOR_DEAD) {
                    sendPrivateChat(
                        msg,
                        currentRoomInfo,
                        thisUser,
                        ID_OF_CHAT_FOR_DEAD
                    );
                }
            } else {
                if (
                    messageSender.chatContainerId === ID_OF_PUBLIC_CHAT &&
                    currentGamePhase !== GamePhase.NIGHT
                ) {
                    sendPublicChat(msg, currentRoomInfo, thisUser);
                } else {
                    sendPrivateChat(
                        msg,
                        currentRoomInfo,
                        thisUser,
                        messageSender.chatContainerId
                    );
                }
            }
        }
    };

    const keyEventListener = (ev: KeyboardEvent) => {
        if (ev.key === "Enter") {
            if (chatInputRef.current) {
                messageSender.sendMessage(chatInputRef.current.value);
                chatInputRef.current.value = "";
            }
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
        const chatContainer = chatContainerMap.get(ID_OF_PUBLIC_CHAT);
        if (chatContainer) {
            if (
                chatContainer.chatLogs.length > 0 &&
                chatContainer.chatLogs[0].senderId === MESSAGE_SEPEARTION_ID
            ) {
                return;
            }
            const newChatContainer: ChatContainer = {
                ...chatContainer,
                chatLogs: [
                    {
                        senderId: MESSAGE_SEPEARTION_ID,
                        message: "",
                        timestamp: new Date().getTime(),
                        containerId: ID_OF_PUBLIC_CHAT,
                        isMe: false
                    },
                    ...chatContainer.chatLogs
                ]
            };
            chatContainerMap.set(chatContainer.id, newChatContainer);
            if (chatContainer.id === currentChatContainer.id) {
                setCurrentChatContainer(newChatContainer);
                messageSender.chatContainerId = newChatContainer.id;
            }
        }
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
        if (newChatContainer.id === ID_OF_CLEAR_CHAT_CONTAINER) {
            const keyIterator = chatContainerMap.keys();
            while (true) {
                const keyNext = keyIterator.next();
                if (keyNext.done) {
                    break;
                }
                const key = keyNext.value;
                chatContainerMap.delete(key);
            }
            setChatContainerTabs(extractKeysAndNames(chatContainerMap));
        } else {
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
                        chatContainerMap.set(
                            newChatContainer.id,
                            newChatContainer
                        );
                        if (newChatContainer.id === currentChatContainer.id) {
                            setCurrentChatContainer(newChatContainer);
                            messageSender.chatContainerId = newChatContainer.id;
                        }
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
                    disabled={
                        (currentChatContainer.id === ID_OF_PUBLIC_CHAT &&
                            currentGamePhase === GamePhase.NIGHT) ||
                        (thisUser.isDead &&
                            currentChatContainer.id !== ID_OF_CHAT_FOR_DEAD)
                    }
                />
                <button
                    className="btn--send-message"
                    type="button"
                    onClick={() => {
                        messageSender.sendMessage(chatInputRef.current!.value);
                        chatInputRef.current!.value = "";
                    }}
                    disabled={
                        (currentChatContainer.id === ID_OF_PUBLIC_CHAT &&
                            currentGamePhase === GamePhase.NIGHT) ||
                        (thisUser.isDead &&
                            currentChatContainer.id !== ID_OF_CHAT_FOR_DEAD)
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
    const ts = `${time.getHours()}:${time.getMinutes()}:${time.getSeconds()}`;

    const wrapperClassName =
        "chat-item-wrapper" +
        (senderId < 0 ? " system-message" : "") +
        (isMe ? " my-message" : "");

    const systemMessageClass =
        senderId < 0
            ? `chat-item ${
                  systemMessageTypeMap.get(senderId as SystemMessengerId)
                      ?.className
              }`
            : "";

    const chatItemClass = "chat-item";
    return (
        <div className="chat-item-container">
            {senderId === MESSAGE_SEPEARTION_ID ? (
                <div
                    className={wrapperClassName}
                    style={{
                        height: "1px",
                        backgroundColor: "#343a40",
                        width: "100%"
                    }}
                />
            ) : (
                <div className={wrapperClassName}>
                    {senderId < 0 ? (
                        <div className={systemMessageClass}>
                            <div className="chat-content">
                                <p className="message">{message}</p>
                            </div>
                            <p className="sended-time">{ts}</p>
                        </div>
                    ) : (
                        <div className={chatItemClass}>
                            <p className="sender">
                                {gameManager.getUser(senderId)!.username}
                            </p>
                            <div className="chat-content">
                                <p className="message">{message}</p>
                            </div>
                            <p className="sended-time">{ts}</p>
                        </div>
                    )}
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

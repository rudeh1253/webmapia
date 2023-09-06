import {useState, useRef, useEffect} from "react";
import {ChatStorage, GamePhase, UserInfo} from "../../type/gameDomainType";
import {iChatStorage} from "../../util/initialState";
import {ChatItem} from "./RoomSubcomponent";
import strResource from "../../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import ChatManager from "../../chat/ChatManager";
import InvalidArgumentError from "../../error/InvalidArgumentError";
import {ErrorCode} from "../../error/ErrorCode";

var chatManager: ChatManager;

const ID_OF_PUBLIC_CHAT = 1;

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
        if (!chatManager) {
            try {
                const gameId = currentRoomInfo.roomInfo.roomId;
                chatManager = ChatManager.getInstance(gameId, thisUser);
                chatManager.dispatch = dispatch;
            } catch (err) {
                if (err instanceof InvalidArgumentError) {
                    switch (err.errorCode) {
                        case ErrorCode.GAME_ID_IS_DEFAULT:
                            console.error(
                                "Game id is invalid. Maybe currentRoomInfo of redux hook is not set because the latency of communication with the server"
                            );
                            break;
                        case ErrorCode.USER_INFO_IS_DEFAULT:
                            console.error(
                                "User info is invalid. Maybe thisUserInfo of redux hook is not set because the latency of communication with the server"
                            );
                            break;
                        default:
                            console.error("Something problem");
                    }
                }
            }
        }
    }, [thisUser, currentRoomInfo]);

    useEffect(() => {
        if (currentGamePhase === GamePhase.GAME_END) {
            resetChatStorage();
        }
    }, [currentGamePhase]);

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
                            chatManager.sendChat(
                                currentChatStorage.participants,
                                chatInputRef.current!.value,
                                currentChatStorage.id === ID_OF_PUBLIC_CHAT
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

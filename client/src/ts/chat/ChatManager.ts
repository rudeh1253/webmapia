import {ErrorCode} from "../error/ErrorCode";
import InvalidArgumentError from "../error/InvalidArgumentError";
import SocketClient from "../sockjs/SocketClient";
import {PublicChatMessage, UserInfo} from "../type/gameDomainType";
import {SOCKET_SEND_CHAT_PRIVATE, SOCKET_SEND_CHAT_PUBLIC} from "../util/const";

var sockClient: SocketClient;

export default class ChatManager {
    private static singleton: ChatManager;

    private _gameId: number;
    private _user: UserInfo;
    private _dispatch: any;

    private constructor(gameId: number, user: UserInfo) {
        this._gameId = gameId;
        this._user = user;
        this._dispatch = null;
        SocketClient.getInstance().then((result) => (sockClient = result));
    }

    public static getInstance(gameId: number, user: UserInfo): ChatManager {
        if (this.singleton == null) {
            if (gameId === -1 || user.userId === -1) {
                const errCode =
                    gameId === -1
                        ? ErrorCode.GAME_ID_IS_DEFAULT
                        : ErrorCode.USER_INFO_IS_DEFAULT;
                throw new InvalidArgumentError(errCode);
            }
            this.singleton = new ChatManager(gameId, user);
        }
        return this.singleton;
    }

    public set dispatch(dispatch: any) {
        this._dispatch = dispatch;
    }

    public async sendChat(to: UserInfo[], message: string, isPublic: boolean) {
        if (!sockClient) {
            sockClient = await SocketClient.getInstance();
        }
        const body: PublicChatMessage = {
            gameId: this._gameId,
            senderId: this._user.userId,
            message
        };
        const url = isPublic
            ? SOCKET_SEND_CHAT_PUBLIC
            : SOCKET_SEND_CHAT_PRIVATE;
        sockClient.sendMessage(url, {}, body);
    }
}

import {Chat, UserInfo} from "../type/gameDomainType";

export function UserItem({userId, username, characterCode, isDead}: UserInfo) {
    return (
        <div>
            <p>{userId}</p>
            <p>{username}</p>
            <p>{characterCode}</p>
            <p>{isDead.toString()}</p>
        </div>
    );
}

export function ChatItem({sender, message, timestamp, isMe}: Chat) {
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

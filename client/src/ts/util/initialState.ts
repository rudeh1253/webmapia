import {UserState} from "../component/room/Room";
import {Chat} from "../type/gameDomainType";

export const iNewUserState: UserState = {
    stateType: null,
    userInfo: {
        userId: -1,
        username: "",
        characterCode: null,
        isDead: false
    }
};

export const iDelayStateForNewUser: UserState = {
    stateType: null,
    userInfo: {
        userId: -1,
        username: "",
        characterCode: null,
        isDead: false
    }
};

export const iNewChat: Chat = {
    senderId: -1,
    message: "",
    timestamp: -1,
    isPublic: false,
    isMe: false
};

export const iDelayStateForNewChat: Chat = {
    senderId: -1,
    message: "",
    timestamp: -1,
    isPublic: false,
    isMe: false
};

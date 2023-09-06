import {UserState} from "../component/room/Room";
import {Chat, ChatStorage} from "../type/gameDomainType";

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

export const iChatStorage: ChatStorage = {
    id: -1,
    participants: [],
    name: "",
    chatLogs: []
};

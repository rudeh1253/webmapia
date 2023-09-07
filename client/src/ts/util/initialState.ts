import {UserState} from "../component/room/Room";
import {Chat, ChatContainer} from "../type/gameDomainType";

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

export const iChatStorage: ChatContainer = {
    id: -1,
    participants: [],
    name: "DEFAULT",
    chatLogs: []
};

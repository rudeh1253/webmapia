import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {ChatContainer} from "../../type/gameDomainType";

const initialState: ChatContainer = {
    id: -1,
    participants: [],
    name: "",
    chatLogs: []
};

const newChatContainerSlice = createSlice({
    name: "newChatContainer",
    initialState,
    reducers: {
        setNewChatContainer(state, action: PayloadAction<ChatContainer>) {
            const dat = action.payload;
            state.id = dat.id;
            state.participants = dat.participants;
            state.name = dat.name;
            state.chatLogs = dat.chatLogs;
        }
    }
});

export const {setNewChatContainer} = newChatContainerSlice.actions;
export default newChatContainerSlice.reducer;

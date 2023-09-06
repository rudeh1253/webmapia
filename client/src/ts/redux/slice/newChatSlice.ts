import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {Chat} from "../../type/gameDomainType";

const initialState: Chat = {
    senderId: -1,
    message: "",
    timestamp: -1,
    containerId: -1,
    isMe: false
};

const newChatSlice = createSlice({
    name: "newChat",
    initialState,
    reducers: {
        setNewChat(state, action: PayloadAction<Chat>) {
            const dat = action.payload;
            state.senderId = dat.senderId;
            state.message = dat.message;
            state.timestamp = dat.timestamp;
            state.containerId = dat.containerId;
            state.isMe = dat.isMe;
        }
    }
});

export const {setNewChat} = newChatSlice.actions;
export default newChatSlice.reducer;

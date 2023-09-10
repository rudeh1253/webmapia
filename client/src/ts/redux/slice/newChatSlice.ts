import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {Chat} from "../../type/gameDomainType";

const initialState: Chat[] = [
    {
        senderId: -1,
        message: "",
        timestamp: -1,
        containerId: -1,
        isMe: false
    }
];

const newChatSlice = createSlice({
    name: "newChat",
    initialState,
    reducers: {
        setNewChat(state, action: PayloadAction<Chat[]>) {
            state = action.payload;
            return action.payload;
        }
    }
});

export const {setNewChat} = newChatSlice.actions;
export default newChatSlice.reducer;

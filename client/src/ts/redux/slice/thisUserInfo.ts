import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {UserInfo} from "../../type/gameDomainType";

const initialState: UserInfo = {
    userId: -1,
    username: "",
    characterCode: null,
    isDead: false
}

export const thisUserInfo = createSlice({
    name: "thisUserInfo",
    initialState,
    reducers: {
        setThisUserInfo(state, action: PayloadAction<UserInfo>) {
            state.username = action.payload.username;
            state.characterCode = action.payload.characterCode;
            state.userId = action.payload.userId;
            state.isDead = action.payload.isDead;
        }
    } 
});

export const {setThisUserInfo} = thisUserInfo.actions;
export default thisUserInfo.reducer;

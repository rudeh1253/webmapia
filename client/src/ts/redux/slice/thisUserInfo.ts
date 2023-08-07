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
            state = action.payload;
        }
    } 
});

export const {setThisUserInfo} = thisUserInfo.actions;
export default thisUserInfo.reducer;

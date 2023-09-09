import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {UserInfo} from "../../type/gameDomainType";

const initialState: UserInfo[] = [];

const usersInRoomSlice = createSlice({
    name: "usersInRoom",
    initialState,
    reducers: {
        setUsersInRoom(state, action: PayloadAction<UserInfo[]>) {
            state = action.payload;
            return state;
        }
    }
});

export const {setUsersInRoom} = usersInRoomSlice.actions;
export default usersInRoomSlice.reducer;

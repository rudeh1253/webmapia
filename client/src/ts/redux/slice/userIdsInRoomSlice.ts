import {PayloadAction, createSlice} from "@reduxjs/toolkit";

const initialState: number[] = [];

const userIdsInRoomSlice = createSlice({
    name: "userIdsInRoom",
    initialState,
    reducers: {
        setUserIdsInRoom(state, action: PayloadAction<number[]>) {
            state = action.payload;
            return state;
        }
    }
});

export const {setUserIdsInRoom} = userIdsInRoomSlice.actions;
export default userIdsInRoomSlice.reducer;

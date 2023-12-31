import {PayloadAction, createSlice} from "@reduxjs/toolkit";

const initialState = -1;

export const timeCountSlice = createSlice({
    name: "timeCount",
    initialState,
    reducers: {
        decrementTime(state, action: PayloadAction<number>) {
            return state - 1;
        },
        setTimeCount(state, action: PayloadAction<number>) {
            return action.payload;
        }
    }
});

export const {decrementTime, setTimeCount} = timeCountSlice.actions;
export default timeCountSlice.reducer;

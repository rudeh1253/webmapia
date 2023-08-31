import {PayloadAction, createSlice} from "@reduxjs/toolkit";

const initialState = false;

export const gameSwitchSlice = createSlice({
    name: "gameSwitchSlice",
    initialState,
    reducers: {
        setGameSwitch(state, action: PayloadAction<boolean>) {
            return action.payload;
        }
    }
});

export const {setGameSwitch} = gameSwitchSlice.actions;
export default gameSwitchSlice.reducer;

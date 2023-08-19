import {PayloadAction, createSlice} from "@reduxjs/toolkit";

const initialState = false;

export const gameSwitchSlice = createSlice({
    name: "gameSwitchSlice",
    initialState,
    reducers: {
        setGameSwitchSlice(state, action: PayloadAction<boolean>) {
            return action.payload;
        }
    }
});

export const {setGameSwitchSlice} = gameSwitchSlice.actions;
export default gameSwitchSlice.reducer;

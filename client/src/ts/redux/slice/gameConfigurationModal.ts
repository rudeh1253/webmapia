import {PayloadAction, createSlice} from "@reduxjs/toolkit";

const initialState = false;

export const gameConfigurationModal = createSlice({
    name: "gameConfigurationModal",
    initialState,
    reducers: {
        setGameConfigurationModal(state, action: PayloadAction<boolean>) {
            return action.payload;
        }
    }
});

export const {setGameConfigurationModal} = gameConfigurationModal.actions;
export default gameConfigurationModal.reducer;

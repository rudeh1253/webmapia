import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {GameSetting} from "../../type/gameDomainType";

const initialState: GameSetting = {
    discussionTimeSeconds: 90,
    voteTimeSeconds: 30,
    nightTimeSeconds: 90
};

export const gameConfiguration = createSlice({
    name: "gameConfiguration",
    initialState,
    reducers: {
        setGameConfiguration(state, action: PayloadAction<GameSetting>) {
            state.discussionTimeSeconds = action.payload.discussionTimeSeconds;
            state.voteTimeSeconds = action.payload.voteTimeSeconds;
            state.nightTimeSeconds = action.payload.nightTimeSeconds;
        }
    }
});

export const {setGameConfiguration} = gameConfiguration.actions;
export default gameConfiguration.reducer;

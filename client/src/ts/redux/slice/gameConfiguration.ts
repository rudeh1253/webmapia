import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {GameSetting} from "../../type/gameDomainType";
import { DEFAULT_TIME_CONFIGURATION } from "../../util/const";

const initialState: GameSetting = DEFAULT_TIME_CONFIGURATION

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

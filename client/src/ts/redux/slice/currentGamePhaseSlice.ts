import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {GamePhase} from "../../type/gameDomainType";

const initialState: {value: GamePhase} = {value: GamePhase.CHARACTER_DISTRIBUTION};

export const currentGamePhaseSlice = createSlice({
    name: "currentGamePhase",
    initialState,
    reducers: {
        setCurrentGamePhase(state, action: PayloadAction<GamePhase>) {
            state.value = action.payload;
        }
    }
});

export const {setCurrentGamePhase} = currentGamePhaseSlice.actions;
export default currentGamePhaseSlice.reducer;

import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {PhaseResult} from "../../type/gameDomainType";

const initialState: PhaseResult = {
    winner: null,
    users: []
};

const phaseResultInfoSlice = createSlice({
    name: "phaseResultInfo",
    initialState,
    reducers: {
        setPhaseResultInfo(state, action: PayloadAction<PhaseResult>) {
            state.winner = action.payload.winner;
            state.users = action.payload.users;
            return state;
        }
    }
});

export const {setPhaseResultInfo} = phaseResultInfoSlice.actions;
export default phaseResultInfoSlice.reducer;

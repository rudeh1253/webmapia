import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {CharacterDistribution} from "../../type/gameDomainType";

const initialState: CharacterDistribution = {
    // TODO: Later, all values must be set to 0.
    BETRAYER: 1,
    CITIZEN: 1,
    DETECTIVE: 1,
    FOLLOWER: 0,
    GUARD: 1,
    HUMAN_MOUSE: 0,
    MEDIUMSHIP: 0,
    MURDERER: 0,
    NOBILITY: 1,
    PREDICTOR: 0,
    SECRET_SOCIETY: 0,
    SOLDIER: 0,
    SUCCESSOR: 0,
    TEMPLAR: 0,
    WOLF: 1
};

export const characterDistributionSlice = createSlice({
    name: "characterDistribution",
    initialState,
    reducers: {
        setCharacterDistribution(
            state,
            action: PayloadAction<CharacterDistribution>
        ) {
            state.BETRAYER = action.payload.BETRAYER;
            state.CITIZEN = action.payload.CITIZEN;
            state.DETECTIVE = action.payload.DETECTIVE;
            state.FOLLOWER = action.payload.FOLLOWER;
            state.GUARD = action.payload.GUARD;
            state.HUMAN_MOUSE = action.payload.HUMAN_MOUSE;
            state.MEDIUMSHIP = action.payload.MEDIUMSHIP;
            state.MURDERER = action.payload.MURDERER;
            state.NOBILITY = action.payload.NOBILITY;
            state.PREDICTOR = action.payload.PREDICTOR;
            state.SECRET_SOCIETY = action.payload.SECRET_SOCIETY;
            state.SOLDIER = action.payload.SOLDIER;
            state.SUCCESSOR = action.payload.SUCCESSOR;
            state.TEMPLAR = action.payload.TEMPLAR;
            state.WOLF = action.payload.WOLF;
        }
    }
});

export const {setCharacterDistribution} = characterDistributionSlice.actions;
export default characterDistributionSlice.reducer;

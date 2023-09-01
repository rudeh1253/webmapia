import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {CharacterDistribution} from "../../type/gameDomainType";

const initialState: CharacterDistribution = {
    BETRAYER: 0,
    CITIZEN: 0,
    DETECTIVE: 0,
    FOLLOWER: 0,
    GUARD: 0,
    HUMAN_MOUSE: 0,
    MEDIUMSHIP: 0,
    MURDERER: 0,
    NOBILITY: 0,
    PREDICTOR: 0,
    SECRET_SOCIETY: 0,
    SOLDIER: 0,
    SUCCESSOR: 0,
    TEMPLAR: 0,
    WOLF: 0
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

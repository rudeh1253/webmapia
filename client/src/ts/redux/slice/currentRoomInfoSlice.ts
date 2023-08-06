import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RoomInfo} from "../../type/gameDomainType";

export interface CurrentRoomInfoInitialState {
    roomInfo: RoomInfo;
}

const currentRoomInfoInitialState: CurrentRoomInfoInitialState = {
    roomInfo: {
        roomId: -1,
        roomName: "",
        hostId: -1,
        numOfUsers: -1
    }
};

export const currentRoomInfoSlice = createSlice({
    name: "currentRoomInfo",
    initialState: currentRoomInfoInitialState,
    reducers: {
        setCurrentRoomInfo(state, action: PayloadAction<RoomInfo>) {
            state.roomInfo = action.payload;
        }
    }
});

export const {setCurrentRoomInfo} = currentRoomInfoSlice.actions;
export default currentRoomInfoSlice.reducer;

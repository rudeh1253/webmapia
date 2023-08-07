import {configureStore} from "@reduxjs/toolkit";
import currentRoomInfoSlice from "./slice/currentRoomInfoSlice";
import thisUserInfoSlice from "./slice/thisUserInfo";

export const store = configureStore({
    reducer: {
        currentRoomInfo: currentRoomInfoSlice,
        thisUserInfo: thisUserInfoSlice
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

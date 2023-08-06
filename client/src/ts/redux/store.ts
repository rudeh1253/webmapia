import { configureStore } from "@reduxjs/toolkit";
import currentRoomInfoSlice from "./slice/currentRoomInfoSlice";

export const store = configureStore({
    reducer: {
        currentRoomInfo: currentRoomInfoSlice,
    },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

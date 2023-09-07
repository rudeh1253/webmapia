import {configureStore} from "@reduxjs/toolkit";
import currentRoomInfoSlice from "./slice/currentRoomInfoSlice";
import thisUserInfoSlice from "./slice/thisUserInfo";
import gameConfiguration from "./slice/gameConfiguration";
import gameConfigurationModal from "./slice/gameConfigurationModal";
import gameSwitch from "./slice/GameSwitchSlice";
import currentGamePhaseSlice from "./slice/currentGamePhaseSlice";
import characterDistributionSlice from "./slice/characterDistributionSlice";
import timeCountSlice from "./slice/timeCountSlice";
import newChatSlice from "./slice/newChatSlice";
import newChatContainerSlice from "./slice/newChatContainerSlice";

export const store = configureStore({
    reducer: {
        currentRoomInfo: currentRoomInfoSlice,
        thisUserInfo: thisUserInfoSlice,
        gameConfiugraion: gameConfiguration,
        gameConfigurationModal: gameConfigurationModal,
        gameSwitch,
        currentGamePhase: currentGamePhaseSlice,
        characterDistribution: characterDistributionSlice,
        timeCount: timeCountSlice,
        newChat: newChatSlice,
        newChatContainer: newChatContainerSlice
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

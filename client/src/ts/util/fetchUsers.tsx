import {UserInfo} from "../type/gameDomainType";
import axios from "axios";
import {CommonResponse, UserResponse} from "../type/responseType";
import {CurrentRoomInfoInitialState} from "../redux/slice/currentRoomInfoSlice";
import {REST_GAME_USER} from "./const";

export async function fetchUsers(currentRoomInfo: CurrentRoomInfoInitialState) {
    const fetchedUsers = await axios.get<CommonResponse<UserResponse[]>>(
        REST_GAME_USER(currentRoomInfo.roomInfo.roomId)
    );
    const u: UserInfo[] = [];
    fetchedUsers.data.data.forEach((us) =>
        u.push({
            userId: us.userId,
            username: us.username,
            characterCode: null,
            isDead: false
        })
    );
    return u;
}

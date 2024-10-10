import {useEffect, useRef, useState} from "react";
import strResource from "../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../redux/hook";
import {CommonResponse, RoomListResponse} from "../type/responseType";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import {REST_GAME_ROOM} from "../util/const";
import {RoomCreationModal, RoomItem} from "./HomeSubcomponents";
import "../../css/Home.css";
import axiosGameService from "../network/axios/axiosGameService";

export default function Home() {
    const [roomCreationModal, setRoomCreationModal] = useState<boolean>(false);
    const [roomList, setRoomList] = useState<RoomListResponse>();

    const searchKeywordInput = useRef<HTMLInputElement>(null);

    const thisUserInfo = useAppSelector((state) => state.thisUserInfo);

    const dispatch = useAppDispatch();

    useEffect(() => {
        init();
    }, []);

    const init = async () => {
        await getRoomList();
    };

    const getRoomList = async (roomName?: string) => {
        if (roomName) {
            // If keyword exists
        } else {
            const roomInfoResponses = await axiosGameService.get<
                CommonResponse<RoomListResponse>
            >(REST_GAME_ROOM);
            console.log(roomInfoResponses);

            setRoomList(roomInfoResponses.data.content);
        }
    };

    return (
        <div className="home-container">
            {roomCreationModal ? (
                <RoomCreationModal setModalState={setRoomCreationModal} />
            ) : null}

            <div className="info-container">
                <div className="user-info">
                    <div className="input-container">
                        <label
                            className="username-input-label"
                            htmlFor="username-input"
                        >
                            {strResource.home.usernameInputLabel}
                        </label>
                        <input
                            id="username-input"
                            type="text"
                            onChange={(e) =>
                                dispatch(
                                    setThisUserInfo({
                                        ...thisUserInfo,
                                        username: e.target.value
                                    })
                                )
                            }
                            disabled={roomCreationModal}
                        />
                    </div>
                </div>
                <div className="function-container">
                    <div className="button-container">
                        <button
                            className="room-create-btn"
                            type="button"
                            onClick={() => setRoomCreationModal(true)}
                            disabled={roomCreationModal}
                        >
                            {strResource.home.createRoom}
                        </button>
                        <button
                            className="reload-btn"
                            type="button"
                            onClick={() => getRoomList()}
                            disabled={roomCreationModal}
                        >
                            {strResource.home.reload}
                        </button>
                    </div>
                </div>
            </div>
            <div className="room-container">
                <div className="input-container room-search">
                    <input
                        className="search-keyword-input"
                        type="text"
                        ref={searchKeywordInput}
                        disabled={roomCreationModal}
                    />
                    <button
                        className="search-btn"
                        type="button"
                        onClick={() => {
                            const searchKeyword =
                                searchKeywordInput.current?.value;
                            getRoomList(searchKeyword);
                        }}
                        disabled={roomCreationModal}
                    >
                        {strResource.home.search}
                    </button>
                </div>
                <div className="room-item-container">

                </div>
            </div>
        </div>
    );
}

import {useEffect, useRef, useState} from "react";
import strResource from "../../resource/string.json";
import {RoomInfo} from "../type/gameDomainType";
import {useAppDispatch, useAppSelector} from "../redux/hook";
import axios from "axios";
import {CommonResponse, RoomInfoResponse} from "../type/responseType";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import SocketClient from "../sockjs/SocketClient";
import {REST_GAME_ROOM} from "../util/const";
import {RoomCreationModal, RoomItem} from "./HomeSubcomponents";
import "../../css/Home.css";

var sockClient: SocketClient;

export default function Home() {
    const [roomCreationModal, setRoomCreationModal] = useState<boolean>(false);
    const [roomList, setRoomList] = useState<Array<RoomInfo>>([]);

    const searchKeywordInput = useRef<HTMLInputElement>(null);

    const thisUserInfo = useAppSelector((state) => state.thisUserInfo);

    const dispatch = useAppDispatch();

    const init = async () => {
        await getRoomList();
        if (!sockClient) {
            sockClient = await SocketClient.getInstance();
        }
    };

    const getRoomList = async (keyword?: string) => {
        if (keyword) {
            // If keyword exists
        } else {
            const roomInfoResponses = await axios.get<
                CommonResponse<RoomInfoResponse[]>
            >(REST_GAME_ROOM);
            const roomInfo: RoomInfo[] = [];
            roomInfoResponses.data.data.forEach((v) =>
                roomInfo.push({
                    roomId: v.roomId,
                    roomName: v.roomName,
                    hostId: v.hostId,
                    numOfUsers: 0
                })
            );
            setRoomList(roomInfo);
        }
    };

    useEffect(() => {
        init();
    }, []);

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
                    {roomList.map((item) => (
                        <RoomItem
                            key={item.roomId}
                            roomId={item.roomId}
                            roomName={item.roomName}
                            hostId={item.hostId}
                            numOfUsers={item.numOfUsers}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
}

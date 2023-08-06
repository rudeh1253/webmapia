import {useEffect, useRef, useState} from "react";
import data from "../../resource/string.json";
import {RoomInfo} from "../type/gameDomainType";
import {useAppDispatch} from "../redux/hook";
import {setCurrentRoomInfo} from "../redux/slice/currentRoomInfoSlice";
import { useNavigate } from "react-router-dom";

export default function Home() {
    const [roomCreationModal, setRoomCreationModal] = useState<boolean>(false);
    const [roomList, setRoomList] = useState<Array<RoomInfo>>([]);

    const usernameInput = useRef<HTMLInputElement>(null);
    const searchKeywordInput = useRef<HTMLInputElement>(null);

    const getRoomList = (keyword?: string) => {
        // TODO: We will get room data from server
        if (keyword) {
            // If keyword exists
        } else {
            // Temp
            const room1: RoomInfo = {
                roomId: 1,
                hostId: 13,
                roomName: "Temp1",
                numOfUsers: 10
            };
            const room2: RoomInfo = {
                roomId: 2,
                hostId: 15,
                roomName: "Temp2",
                numOfUsers: 8
            };
            setRoomList([room1, room2]);
        }
    };

    useEffect(() => {
        getRoomList();
    }, []);

    return (
        <div className="container">
            {roomCreationModal ? (
                <RoomCreationModal setModalState={setRoomCreationModal} />
            ) : null}
            <div className="user-info">
                <div className="input-container">
                    <label
                        className="username-input-label"
                        htmlFor="username-input"
                    >
                        {data.home.usernameInputLabel}
                    </label>
                    <input
                        id="username-input"
                        type="text"
                        ref={usernameInput}
                    />
                </div>
            </div>
            <div className="game-container">
                <div className="function-container">
                    <div className="room-search-container">
                        <input
                            className="search-keyword-input"
                            type="text"
                            ref={searchKeywordInput}
                        />
                        <button
                            className="search-btn"
                            type="button"
                            onClick={() => {
                                const searchKeyword =
                                    searchKeywordInput.current?.value;
                                getRoomList(searchKeyword);
                            }}
                        >
                            {data.home.search}
                        </button>
                    </div>
                    <button
                        className="room-create-btn"
                        type="button"
                        onClick={() => setRoomCreationModal(true)}
                    >
                        {data.home.createRoom}
                    </button>
                    <button className="reload-btn" type="button">
                        {data.home.reload}
                    </button>
                </div>
                <div className="room-container">
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

interface ModalProps {
    setModalState: React.Dispatch<React.SetStateAction<boolean>>;
}

function RoomCreationModal({setModalState}: ModalProps) {
    return (
        <div className="modal">
            <button type="button" onClick={() => setModalState(false)}>
                {data.home.close}
            </button>
            <div className="room-info-input-container">
                <label htmlFor="room-info-input">
                    {data.home.inputRoomName}
                </label>
                <input type="text" id="room-info-input" />
            </div>
            <button type="button">{data.home.createRoom}</button>
        </div>
    );
}

function RoomItem({roomId, roomName, hostId, numOfUsers}: RoomInfo, ) {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const onClickEnterBtn = () => {
        dispatch(
            setCurrentRoomInfo({
                roomId,
                roomName,
                hostId,
                numOfUsers
            })
        );
        navigate("/room");
    };

    return (
        <div className="room-item">
            <p>{roomName}</p>
            <p>{numOfUsers}</p>
            <button type="button" onClick={onClickEnterBtn}>
                {data.home.enter}
            </button>
        </div>
    );
}

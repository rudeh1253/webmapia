import {useEffect, useRef, useState} from "react";
import strResource from "../../resource/string.json";
import serverSpecResource from "../../resource/secret/server-spec.json";
import {RoomInfo} from "../type/gameDomainType";
import {useAppDispatch, useAppSelector} from "../redux/hook";
import {setCurrentRoomInfo} from "../redux/slice/currentRoomInfoSlice";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {CommonResponse, RoomInfoResponse} from "../type/responseType";
import {RoomCreationRequest, UserRequest} from "../type/requestType";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import SocketClient from "../sockjs/SocketClient";

export default function Home() {
    const [roomCreationModal, setRoomCreationModal] = useState<boolean>(false);
    const [roomList, setRoomList] = useState<Array<RoomInfo>>([]);

    const searchKeywordInput = useRef<HTMLInputElement>(null);

    const thisUserInfo = useAppSelector((state) => state.thisUserInfo);

    const dispatch = useAppDispatch();

    const getRoomList = async (keyword?: string) => {
        if (keyword) {
            // If keyword exists
        } else {
            const roomInfoResponses = await axios.get<
                CommonResponse<RoomInfoResponse[]>
            >(
                serverSpecResource.restApiUrl +
                    serverSpecResource.restEndpoints.gameRoom
            );
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
                            {strResource.home.search}
                        </button>
                    </div>
                    <button
                        className="room-create-btn"
                        type="button"
                        onClick={() => setRoomCreationModal(true)}
                    >
                        {strResource.home.createRoom}
                    </button>
                    <button
                        className="reload-btn"
                        type="button"
                        onClick={() => getRoomList()}
                    >
                        {strResource.home.reload}
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
    const thisUserInfo = useAppSelector((state) => state.thisUserInfo);

    const roomNameInputRef = useRef<HTMLInputElement>(null);

    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    return (
        <div className="modal">
            <button type="button" onClick={() => setModalState(false)}>
                {strResource.home.close}
            </button>
            <div className="room-name-input-container">
                <label htmlFor="room-name-input">
                    {strResource.home.inputRoomName}
                </label>
                <input
                    type="text"
                    id="room-name-input"
                    ref={roomNameInputRef}
                />
            </div>
            <button
                type="button"
                onClick={async () => {
                    const roomName = roomNameInputRef.current?.value!;
                    const hostId = await generateId();
                    dispatch(
                        setThisUserInfo({...thisUserInfo, userId: hostId})
                    );
                    const roomCreationRequestBody: RoomCreationRequest = {
                        gameName: roomName,
                        hostId,
                        hostName: thisUserInfo.username
                    };
                    const roomInfo = await axios.post<
                        CommonResponse<RoomInfoResponse>
                    >(
                        serverSpecResource.restApiUrl +
                            serverSpecResource.restEndpoints.gameRoom,
                        roomCreationRequestBody
                    );
                    dispatch(
                        setCurrentRoomInfo({
                            roomId: roomInfo.data.data.roomId,
                            roomName: roomInfo.data.data.roomName,
                            hostId: roomInfo.data.data.hostId,
                            numOfUsers: roomInfo.data.data.users.length
                        })
                    );
                    navigate("/room");
                }}
            >
                {strResource.home.createRoom}
            </button>
        </div>
    );
}

async function generateId(): Promise<number> {
    const response = await axios.post<CommonResponse<number>>(
        serverSpecResource.restApiUrl + serverSpecResource.restEndpoints.userId
    );
    const generatedId = response.data.data;
    return generatedId;
}

function RoomItem({roomId, roomName, hostId, numOfUsers}: RoomInfo) {
    const thisUserInfo = useAppSelector((state) => state.thisUserInfo);

    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const onClickEnterBtn = async () => {
        dispatch(
            setCurrentRoomInfo({
                roomId,
                roomName,
                hostId,
                numOfUsers
            })
        );
        const userId = await generateId();
        dispatch(setThisUserInfo({...thisUserInfo, userId}));
        const sock = await SocketClient.getInstance();
        const body: UserRequest = {
            notificationType: "USER_ENTERED",
            gameId: roomId,
            userId,
            username: thisUserInfo.username
        };
        await sock.sendMessage("/app/game/user-enter", {}, body);
        navigate("/room");
    };

    return (
        <div className="room-item">
            <p>{roomName}</p>
            <p>{numOfUsers}</p>
            <button type="button" onClick={onClickEnterBtn}>
                {strResource.home.enter}
            </button>
        </div>
    );
}

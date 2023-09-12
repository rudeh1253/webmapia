import {useRef} from "react";
import strResource from "../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../redux/hook";
import {useNavigate} from "react-router-dom";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import {RoomCreationRequest} from "../type/requestType";
import {CommonResponse, RoomInfoResponse} from "../type/responseType";
import {REST_GAME_ROOM, REST_USER_ID} from "../util/const";
import {setCurrentRoomInfo} from "../redux/slice/currentRoomInfoSlice";
import axios from "axios";
import {RoomInfo} from "../type/gameDomainType";

interface ModalProps {
    setModalState: React.Dispatch<React.SetStateAction<boolean>>;
}

export function RoomCreationModal({setModalState}: ModalProps) {
    const thisUserInfo = useAppSelector((state) => state.thisUserInfo);

    const roomNameInputRef = useRef<HTMLInputElement>(null);

    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    return (
        <div className="modal-container">
            <div className="modal">
                <div className="modal-content">
                    <img
                        className="btn--close-modal"
                        src={process.env.PUBLIC_URL + "/close.png"}
                        alt={strResource.home.close}
                        onClick={() => setModalState(false)}
                    />
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
                                setThisUserInfo({
                                    ...thisUserInfo,
                                    userId: hostId
                                })
                            );
                            const roomCreationRequestBody: RoomCreationRequest =
                                {
                                    gameName: roomName,
                                    hostId,
                                    hostName: thisUserInfo.username
                                };
                            const roomInfo = await axios.post<
                                CommonResponse<RoomInfoResponse>
                            >(REST_GAME_ROOM, roomCreationRequestBody);
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
            </div>
        </div>
    );
}

async function generateId(): Promise<number> {
    const response = await axios.post<CommonResponse<number>>(REST_USER_ID);
    const generatedId = response.data.data;
    return generatedId;
}

export function RoomItem({roomId, roomName, hostId, numOfUsers}: RoomInfo) {
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

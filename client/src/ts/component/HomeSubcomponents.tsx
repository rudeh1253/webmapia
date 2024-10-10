import {useRef} from "react";
import strResource from "../../resource/string.json";
import {useAppDispatch, useAppSelector} from "../redux/hook";
import {useNavigate} from "react-router-dom";
import {setThisUserInfo} from "../redux/slice/thisUserInfo";
import {RoomCreationRequest} from "../type/requestType";
import {
    CommonResponse,
    RoomAvailabilityResponse, RoomCreationResponse,
    RoomListResponse
} from "../type/responseType";
import {
    REST_GAME_AVAILABILITY,
    REST_GAME_ROOM
} from "../util/const";
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
                    className="btn--room-creation"
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
                        const roomCreationRequestBody: RoomCreationRequest = {
                            gameName: roomName,
                            hostId,
                            hostName: thisUserInfo.username
                        };
                        const roomInfo = await axios.post<
                            CommonResponse<RoomCreationResponse>
                        >(REST_GAME_ROOM, roomCreationRequestBody);
                        dispatch(
                            setCurrentRoomInfo({
                                roomId: roomInfo.data.content.roomId,
                                roomName: roomInfo.data.content.roomName,
                                hostMemberId: roomInfo.data.content.hostMemberId,
                                numOfUsers: 1
                            })
                        );
                        navigate("/room");
                    }}
                >
                    {strResource.home.createRoom}
                </button>
            </div>
        </div>
    );
}

export function RoomItem({roomId, roomName, hostMemberId, numOfUsers}: RoomInfo) {
    const thisUserInfo = useAppSelector((state) => state.thisUserInfo);

    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const onClickEnterBtn = async () => {
        const availabilityRes = await axios.get<
            CommonResponse<RoomAvailabilityResponse>
        >(REST_GAME_AVAILABILITY(roomId));
        console.log(availabilityRes.data);
        const available = availabilityRes.data.content.available;
        console.log(available);
        if (available) {
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
        }
    };

    return (
        <div className="room-item">
            <p className="room-name">{roomName}</p>
            <button
                className="btn--enter"
                type="button"
                onClick={onClickEnterBtn}
            >
                {strResource.home.enter}
            </button>
        </div>
    );
}

import { useRef, useState } from "react";
import data from "../../resource/string.json";

export default function Home() {
    const [roomCreationModal, setRoomCreationModal] = useState<boolean>(false);
    const usernameInput = useRef<HTMLInputElement>(null);
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
                        <input className="search-keyword-input" type="text" />
                        <button className="search-btn" type="button">
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
                <div className="room-container"></div>
            </div>
        </div>
    );
}

interface ModalProps {
    setModalState: React.Dispatch<React.SetStateAction<boolean>>;
}

function RoomCreationModal({ setModalState }: ModalProps) {
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

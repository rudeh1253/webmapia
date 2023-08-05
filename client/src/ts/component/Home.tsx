import { useRef } from "react";
import data from "../../resource/string.json";

export default function Home() {
    const usernameInput = useRef<HTMLInputElement>(null);
    return (
        <div className="container">
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
                    <button className="room-create-btn" type="button">
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

import {useState, useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import {setGameConfiguration} from "../../redux/slice/gameConfiguration";
import strResource from "../../../resource/string.json";
import {setGameConfigurationModal} from "../../redux/slice/gameConfigurationModal";
import {Chat, UserInfo} from "../../type/gameDomainType";
import {setCharacterDistribution} from "../../redux/slice/characterDistributionSlice";
import GameManager from "../../game/GameManager";

var gameManager = GameManager.getInstance();

type GameConfigurationModalProps = {
    characterConfigurationProps: CharacterConfigurationProps;
};

export function GameConfigurationModal({
    characterConfigurationProps
}: GameConfigurationModalProps) {
    return (
        <div className="configuration-modal-container">
            <TimeConfiguration />
            <CharacterConfiguration
                usersInRoom={characterConfigurationProps.usersInRoom}
            />
        </div>
    );
}

function TimeConfiguration() {
    const gameConfiguration = useAppSelector((state) => state.gameConfiugraion);
    const dispatch = useAppDispatch();

    const onChangeLogic = (
        key: "discussionTimeSeconds" | "voteTimeSeconds" | "nightTimeSeconds",
        val: number
    ) => {
        const config = {...gameConfiguration};
        config[key] = val;
        dispatch(setGameConfiguration(config));
        gameManager.gameSetting = config;
    };
    return (
        <div className="radio-container">
            <div className="radio-component">
                <h6 className="radio-title">
                    {strResource.room.gameConfigurationModal.discussionTime}
                </h6>
                <div className="radio-group">
                    <div className="radio-item">
                        <label htmlFor="discussion-time-60">60</label>
                        <input
                            type="radio"
                            name="discussion-time"
                            id="discussion-time-60"
                            onChange={() =>
                                onChangeLogic("discussionTimeSeconds", 60)
                            }
                        />
                    </div>
                    <div className="radio-item">
                        <label htmlFor="discussion-time-90">90</label>
                        <input
                            type="radio"
                            name="discussion-time"
                            id="discussion-time-90"
                            onChange={() =>
                                onChangeLogic("discussionTimeSeconds", 90)
                            }
                        />
                    </div>
                    <div className="radio-item">
                        <label htmlFor="discussion-time-120">120</label>
                        <input
                            type="radio"
                            name="discussion-time"
                            id="discussion-time-120"
                            onChange={() =>
                                onChangeLogic("discussionTimeSeconds", 120)
                            }
                        />
                    </div>
                </div>
            </div>
            <div className="radio-component">
                <h6 className="radio-title">
                    {strResource.room.gameConfigurationModal.voteTime}
                </h6>
                <div className="radio-group">
                    <div className="radio-item">
                        <label htmlFor="vote-time-60">60</label>
                        <input
                            type="radio"
                            name="vote-time"
                            id="vote-time-60"
                            onChange={() =>
                                onChangeLogic("voteTimeSeconds", 60)
                            }
                        />
                    </div>
                    <div className="radio-item">
                        <label htmlFor="vote-time-90">90</label>
                        <input
                            type="radio"
                            name="vote-time"
                            id="vote-time-90"
                            onChange={() =>
                                onChangeLogic("voteTimeSeconds", 90)
                            }
                        />
                    </div>
                    <div className="radio-item">
                        <label htmlFor="vote-time-120">120</label>
                        <input
                            type="radio"
                            name="vote-time"
                            id="vote-time-120"
                            onChange={() =>
                                onChangeLogic("voteTimeSeconds", 120)
                            }
                        />
                    </div>
                </div>
            </div>
            <div className="radio-component">
                <h6 className="radio-title">
                    {strResource.room.gameConfigurationModal.nightTime}
                </h6>
                <div className="radio-group">
                    <div className="radio-item">
                        <label htmlFor="night-time-60">60</label>
                        <input
                            type="radio"
                            name="night-time"
                            id="night-time-60"
                            onChange={() =>
                                onChangeLogic("nightTimeSeconds", 60)
                            }
                        />
                    </div>
                    <div className="radio-item">
                        <label htmlFor="night-time-90">90</label>
                        <input
                            type="radio"
                            name="night-time"
                            id="night-time-90"
                            onChange={() =>
                                onChangeLogic("nightTimeSeconds", 90)
                            }
                        />
                    </div>
                    <div className="radio-item">
                        <label htmlFor="night-time-120">120</label>
                        <input
                            type="radio"
                            name="night-time"
                            id="night-time-120"
                            onChange={() =>
                                onChangeLogic("nightTimeSeconds", 120)
                            }
                        />
                    </div>
                </div>
            </div>
            <button
                type="button"
                onClick={() => dispatch(setGameConfigurationModal(false))}
            >
                {strResource.room.gameConfigurationModal.close}
            </button>
        </div>
    );
}

type CharacterConfigurationProps = {
    usersInRoom: UserInfo[];
};

function CharacterConfiguration({usersInRoom}: CharacterConfigurationProps) {
    const [sumOfCharacterDistribution, setSumOfCharacterDistribution] =
        useState<number>(0);
    const characterDistribution = useAppSelector(
        (state) => state.characterDistribution
    );
    const dispatch = useAppDispatch();
    const numOfUsers = usersInRoom.length;
    const characters = [
        "BETRAYER",
        "CITIZEN",
        "DETECTIVE",
        "FOLLOWER",
        "GUARD",
        "HUMAN_MOUSE",
        "MEDIUMSHIP",
        "MURDERER",
        "NOBILITY",
        "PREDICTOR",
        "SECRET_SOCIETY",
        "SOLDIER",
        "SUCCESSOR",
        "TEMPLAR",
        "WOLF"
    ];
    useEffect(() => {
        let sum = 0;
        for (let key of characters) {
            const k = key as
                | "BETRAYER"
                | "CITIZEN"
                | "DETECTIVE"
                | "FOLLOWER"
                | "GUARD"
                | "HUMAN_MOUSE"
                | "MEDIUMSHIP"
                | "MURDERER"
                | "NOBILITY"
                | "PREDICTOR"
                | "SECRET_SOCIETY"
                | "SOLDIER"
                | "SUCCESSOR"
                | "TEMPLAR"
                | "WOLF";
            sum += characterDistribution[k];
        }
        setSumOfCharacterDistribution(sum);
    }, [characterDistribution]);
    return (
        <div className="character-config-container">
            {characters.map((elem) => {
                const key = elem as
                    | "BETRAYER"
                    | "CITIZEN"
                    | "DETECTIVE"
                    | "FOLLOWER"
                    | "GUARD"
                    | "HUMAN_MOUSE"
                    | "MEDIUMSHIP"
                    | "MURDERER"
                    | "NOBILITY"
                    | "PREDICTOR"
                    | "SECRET_SOCIETY"
                    | "SOLDIER"
                    | "SUCCESSOR"
                    | "TEMPLAR"
                    | "WOLF";
                return (
                    <div className="character-config">
                        <p>{key}</p>
                        <p>{characterDistribution[key]}</p>
                        <button
                            type="button"
                            onClick={() => {
                                const newState = {...characterDistribution};
                                newState[key]++;
                                console.log(newState[key]);
                                dispatch(setCharacterDistribution(newState));
                            }}
                            disabled={sumOfCharacterDistribution >= numOfUsers}
                        >
                            {strResource.room.gameConfigurationModal.up}
                        </button>
                        <button
                            type="button"
                            onClick={() => {
                                const newState = {...characterDistribution};
                                newState[key]--;
                                dispatch(setCharacterDistribution(newState));
                            }}
                            disabled={characterDistribution[key] <= 0}
                        >
                            {strResource.room.gameConfigurationModal.down}
                        </button>
                    </div>
                );
            })}
        </div>
    );
}

export function ChatItem({senderId, message, timestamp, isMe}: Chat) {
    const time = new Date(timestamp);
    return (
        <div>
            <p>{senderId}</p>
            <p>{message}</p>
            <p>{`${time.getHours()}:${time.getMinutes()}:${time.getSeconds()}/${time.getFullYear()}-${time.getMonth()}-${time.getDay()}`}</p>
        </div>
    );
}

export function UserItem({userId, username, characterCode, isDead}: UserInfo) {
    return (
        <div>
            <p>{userId}</p>
            <p>{username}</p>
            <p>{characterCode}</p>
            <p>{isDead.toString()}</p>
        </div>
    );
}

import {useState, useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../redux/hook";
import {setGameConfiguration} from "../../redux/slice/gameConfiguration";
import strResource from "../../../resource/string.json";
import {setGameConfigurationModal} from "../../redux/slice/gameConfigurationModal";
import {UserInfo} from "../../type/gameDomainType";
import {setCharacterDistribution} from "../../redux/slice/characterDistributionSlice";
import GameManager from "../../game/GameManager";
import {sumCharacterDistribution} from "../../util/utilFunction";
import {characterNameMap} from "../../game/characterNameMap";

var gameManager = GameManager.getInstance();

type GameConfigurationModalProps = {
    characterConfigurationProps: CharacterConfigurationProps;
};

export function GameConfigurationModal({
    characterConfigurationProps
}: GameConfigurationModalProps) {
    const dispatch = useAppDispatch();
    return (
        <div className="cmodal-container">
            <div className="modal-wrapper">
                <button
                    className="btn--close-modals"
                    type="button"
                    onClick={() => dispatch(setGameConfigurationModal(false))}
                >
                    {strResource.room.gameConfigurationModal.close}
                </button>
                <div className="configuration-modal-container">
                    <TimeConfiguration />
                    <CharacterConfiguration
                        userIdsInRoom={
                            characterConfigurationProps.userIdsInRoom
                        }
                    />
                </div>
            </div>
        </div>
    );
}

const timeConfigContent = {
    nightTimeSeconds: {
        name: "night-time",
        header: strResource.room.gameConfigurationModal.nightTime,
        seconds: [30, 60, 90, 120]
    },
    discussionTimeSeconds: {
        name: "discussion-time",
        header: strResource.room.gameConfigurationModal.discussionTime,
        seconds: [30, 60, 90, 120]
    },
    voteTimeSeconds: {
        name: "vote-time",
        header: strResource.room.gameConfigurationModal.voteTime,
        seconds: [30, 60, 90, 120]
    }
};

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
            {Object.keys(timeConfigContent).map((key) => {
                const k = key as
                    | "discussionTimeSeconds"
                    | "voteTimeSeconds"
                    | "nightTimeSeconds";
                return (
                    <div className="radio-component">
                        <p className="radio-title">
                            {timeConfigContent[k].header}
                        </p>
                        {timeConfigContent[k].seconds.map((sec) => {
                            const radioId = `${timeConfigContent[k].name}-${sec}`;
                            return (
                                <div className="radio-item">
                                    <input
                                        type="radio"
                                        name={timeConfigContent[k].name}
                                        id={radioId}
                                        onChange={() => onChangeLogic(k, sec)}
                                        checked={gameConfiguration[k] === sec}
                                    />
                                    <label htmlFor={radioId}>
                                        {sec}
                                        {
                                            strResource.room
                                                .gameConfigurationModal.second
                                        }
                                    </label>
                                </div>
                            );
                        })}
                    </div>
                );
            })}
        </div>
    );
}

type CharacterConfigurationProps = {
    userIdsInRoom: number[];
};

function CharacterConfiguration({
    userIdsInRoom: usersInRoom
}: CharacterConfigurationProps) {
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
        const sum = sumCharacterDistribution(characterDistribution);
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
                        <p>{characterNameMap.get(key)}</p>
                        <p>{characterDistribution[key]}</p>
                        <div className="char-dist-btn-container">
                            <button
                                className="char-dist-btn"
                                type="button"
                                onClick={() => {
                                    const newState = {...characterDistribution};
                                    newState[key]++;
                                    dispatch(
                                        setCharacterDistribution(newState)
                                    );
                                }}
                                disabled={
                                    sumOfCharacterDistribution >= numOfUsers
                                }
                            >
                                {strResource.room.gameConfigurationModal.up}
                            </button>
                            <button
                                className="char-dist-btn"
                                type="button"
                                onClick={() => {
                                    const newState = {...characterDistribution};
                                    newState[key]--;
                                    dispatch(
                                        setCharacterDistribution(newState)
                                    );
                                }}
                                disabled={characterDistribution[key] <= 0}
                            >
                                {strResource.room.gameConfigurationModal.down}
                            </button>
                        </div>
                    </div>
                );
            })}
        </div>
    );
}

export function UserItem({userId, username, characterCode, isDead}: UserInfo) {
    const roomInfo = useAppSelector((state) => state.currentRoomInfo);
    const thisUser = useAppSelector((state) => state.thisUserInfo);

    const classNameOfItem = "user-item" + (isDead ? " dead-user" : "");
    return (
        <li className={classNameOfItem}>
            <p>{username}</p>
            {userId === thisUser.userId ? <p className="self">me</p> : null}
        </li>
    );
}

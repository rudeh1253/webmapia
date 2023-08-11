import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../redux/hook";
import {setGameConfiguration} from "../redux/slice/gameConfiguration";
import strResource from "../../resource/string.json";

export function GameConfigurationModal() {
    const gameConfiguration = useAppSelector((state) => state.gameConfiugraion);
    useEffect(() => {
        console.log(gameConfiguration);
    }, [gameConfiguration]);
    const dispatch = useAppDispatch();
    return (
        <div className="configuration-modal-container">
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            discussionTimeSeconds: 60
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            discussionTimeSeconds: 90
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            discussionTimeSeconds: 120
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            voteTimeSeconds: 60
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            voteTimeSeconds: 90
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            voteTimeSeconds: 120
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            nightTimeSeconds: 60
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            nightTimeSeconds: 90
                                        })
                                    )
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
                                    dispatch(
                                        setGameConfiguration({
                                            ...gameConfiguration,
                                            nightTimeSeconds: 120
                                        })
                                    )
                                }
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

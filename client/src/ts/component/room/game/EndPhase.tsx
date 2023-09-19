import {useState, useEffect} from "react";
import {useAppSelector} from "../../../redux/hook";
import strResource from "../../../../resource/string.json";
import {characterNameMap} from "../../../game/characterNameMap";

export default function EndPhase() {
    const [winnerText, setWinnerText] = useState<string>("");
    const phaseResultInfo = useAppSelector((state) => state.phaseResultInfo);
    const users = phaseResultInfo.users;

    useEffect(() => {
        switch (phaseResultInfo.winner) {
            case "WOLF":
                setWinnerText(
                    `${strResource.game.wolf} ${strResource.game.winnerFaction}`
                );
                break;
            case "HUMAN":
                setWinnerText(
                    `${strResource.game.human} ${strResource.game.winnerFaction}`
                );
                break;
            case "HUMAN_MOUSE":
                setWinnerText(
                    `${strResource.game.humanMouse} ${strResource.game.winnerFaction}`
                );
                break;
            default:
                setWinnerText("");
        }
    }, [phaseResultInfo]);
    return (
        <div>
            <p>{strResource.game.gameEnd}</p>
            <p>{winnerText}</p>
            {users.map((user) => {
                return (
                    <p>{`${user!.username} ${characterNameMap.get(
                        user!.characterCode
                    )}`}</p>
                );
            })}
        </div>
    );
}

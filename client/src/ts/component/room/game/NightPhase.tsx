import strResource from "../../../../resource/string.json";
import skillsPerChars from "../../../game/skillsPerChars";
import {useAppSelector} from "../../../redux/hook";
import SocketClient from "../../../sockjs/SocketClient";
import {SkillType, UserInfo} from "../../../type/gameDomainType";
import {useState, useEffect} from "react";
import {SOCKET_SEND_ACTIVATE_SKILL} from "../../../util/const";
import {SkillActivationRequest} from "../../../type/requestType";
import GameManager from "../../../game/GameManager";

var sockClient: SocketClient;

export default function NightPhase() {
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const skills = skillsPerChars.get(thisUser.characterCode);

    useEffect(() => {
        if (!sockClient) {
            SocketClient.getInstance().then(
                (instance) => (sockClient = instance)
            );
        }
    }, []);
    return (
        <div className="phase-container night-phase">
            {skills?.map((e, idx) => {
                return (
                    <SkillSelection
                        skillName={e.skillName}
                        skillDesc={e.skillDesc}
                        skillType={e.skillType}
                    />
                );
            })}
        </div>
    );
}

type SkillSelectionProps = {
    skillName: string;
    skillDesc: string;
    skillType: SkillType;
};

function SkillSelection({
    skillName,
    skillDesc,
    skillType
}: SkillSelectionProps) {
    const [targetSelection, setTargetSelection] = useState<boolean>(false);
    const [target, setTarget] = useState<UserInfo>();
    const [used, setUsed] = useState<boolean>(false);

    const currentRoomInfo = useAppSelector(
        (state) => state.currentRoomInfo
    ).roomInfo;
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    const currentGamePhase = useAppSelector((state) => state.currentGamePhase);

    useEffect(() => {
        setUsed(false);
    }, [currentGamePhase]);

    return (
        <div className="skill-selection-container">
            <button
                className="target-selection-toggle-btn"
                type="button"
                onClick={() => setTargetSelection(!targetSelection)}
            >
                {skillName}
            </button>
            {targetSelection ? (
                <div className="target-selection">
                    <TargetSelection
                        skillType={skillType}
                        setTarget={setTarget}
                    />
                    <button
                        className="btn--skill-use"
                        type="button"
                        onClick={() => {
                            if (target) {
                                const body: SkillActivationRequest = {
                                    gameId: currentRoomInfo.roomId,
                                    activatorId: thisUser.userId,
                                    targetId: target.userId,
                                    skillType
                                };
                                sockClient.sendMessage(
                                    SOCKET_SEND_ACTIVATE_SKILL,
                                    {},
                                    body
                                );
                                setUsed(true);
                            }
                        }}
                        disabled={used}
                    >
                        Use
                    </button>
                </div>
            ) : null}
            <p className="skill-desc">{skillDesc}</p>
        </div>
    );
}

type TargetSelectionProps = {
    skillType: SkillType;
    setTarget: React.Dispatch<React.SetStateAction<UserInfo | undefined>>;
};

function TargetSelection({skillType, setTarget}: TargetSelectionProps) {
    const gameManager = GameManager.getInstance();
    const userIdsInRoom = useAppSelector((state) => state.userIdsInRoom);
    const [selectedId, setSelectedId] = useState(-1);
    return (
        <div className="target-selection-btn-container">
            {userIdsInRoom.map((id) => {
                const user = gameManager.getUser(id);
                return skillType === "INVESTIGATE_DEAD_CHARACTER" ? (
                    user?.isDead ? (
                        <button
                            className="target-selection-btn"
                            type="button"
                            onClick={() => {
                                setTarget(user);
                                setSelectedId(id);
                            }}
                            disabled={selectedId === id}
                        >
                            {user.username}
                        </button>
                    ) : null
                ) : !user?.isDead ? (
                    <button
                        className="target-selection-btn"
                        type="button"
                        onClick={() => {
                            setTarget(user);
                            setSelectedId(id);
                        }}
                        disabled={selectedId === id}
                    >
                        {user?.username}
                    </button>
                ) : null;
            })}
        </div>
    );
}

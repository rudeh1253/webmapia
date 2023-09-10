import strResource from "../../../../resource/string.json";
import skillsPerChars from "../../../game/skillsPerChars";
import {useAppSelector} from "../../../redux/hook";
import SocketClient from "../../../sockjs/SocketClient";
import {SkillType, UserInfo} from "../../../type/gameDomainType";
import {useState, useEffect} from "react";
import {SOCKET_SEND_ACTIVATE_SKILL} from "../../../util/const";
import {SkillActivationRequest} from "../../../type/requestType";

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
        <div className="game-container night-phase">
            <p>{strResource.game.night}</p>
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

    const currentRoomInfo = useAppSelector(
        (state) => state.currentRoomInfo
    ).roomInfo;
    const thisUser = useAppSelector((state) => state.thisUserInfo);
    return (
        <div className="selection-container">
            <button
                className="selection-btn"
                type="button"
                onClick={() => setTargetSelection(!targetSelection)}
            >
                {skillName}
            </button>
            {targetSelection ? (
                <div>
                    <TargetSelection setTarget={setTarget} />
                    <button
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
                            }
                        }}
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
    setTarget: React.Dispatch<React.SetStateAction<UserInfo | undefined>>;
};

function TargetSelection({setTarget}: TargetSelectionProps) {
    const usersInRoom = useAppSelector((state) => state.usersInRoom);
    return (
        <div>
            {usersInRoom.map((user) => {
                return user.isDead ? null : (
                    <button type="button" onClick={() => setTarget(user)}>
                        {user.username}
                    </button>
                );
            })}
        </div>
    );
}

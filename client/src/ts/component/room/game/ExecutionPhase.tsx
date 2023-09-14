import strResource from "../../../../resource/string.json";

export default function ExecutionPhase() {
    return (
        <div className="phase-container execution-phase">
            <p>{strResource.game.execution}</p>
        </div>
    );
}

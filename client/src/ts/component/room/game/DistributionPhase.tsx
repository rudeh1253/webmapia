import strResource from "../../../../resource/string.json";

export default function DistributionPhase() {
    return (
        <div className="phase-container distribution-phase">
            <p className="distribution-message">
                {strResource.game.inDistribution}
            </p>
        </div>
    );
}

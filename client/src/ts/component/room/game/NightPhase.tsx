import strResource from "../../../../resource/string.json";

export default function NightPhase() {
    return (
        <div className="game-container night-phase">
            <p>{strResource.game.night}</p>
        </div>
    );
}

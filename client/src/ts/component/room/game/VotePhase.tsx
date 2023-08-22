import strResource from "../../../../resource/string.json";

export default function VotePhase() {
    return (
        <div className="game-container vote-phase">
            <p>{strResource.game.vote}</p>
        </div>
    );
}

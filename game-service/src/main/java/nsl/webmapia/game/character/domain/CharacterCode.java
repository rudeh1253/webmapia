package nsl.webmapia.game.character.domain;

public enum CharacterCode {
    WOLF("늑대"),
    BETRAYER("배신자"),
    FOLLOWER("추종자"),
//    PREDICTOR,
    GUARD("경비병"),
//    MEDIUMSHIP,
    DETECTIVE("탐정"),
    SECRET_SOCIETY("비밀결사"),
    NOBILITY("귀족"),
    SOLDIER("군인"),
    TEMPLAR("템플러"),
    CITIZEN("시민"),
    MURDERER("살인자"),
    HUMAN_MOUSE("쥐인간");

    private final String title;

    CharacterCode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}

package nsl.webmapia.game.character.domain;

public enum CharacterCode {
    WOLF("늑대"),
    BETRAYER("배신자"),
    FOLLOWER("추종자"),
    PREDICTOR("예언자"),
    GUARD("경비병"),
    MEDIUMSHIP("영매"),
    DETECTIVE("탐정"),
    SECRET_SOCIETY("비밀결사"),
    NOBILITY("귀족"),
    SOLDIER("군인"),
    TEMPLAR("템플러"),
    CITIZEN("시민"),
    MURDERER("살인자"),
    HUMAN_MOUSE("쥐인간"),

    GOOD_PERSON("선량한 시민");

    private final String title;

    CharacterCode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}

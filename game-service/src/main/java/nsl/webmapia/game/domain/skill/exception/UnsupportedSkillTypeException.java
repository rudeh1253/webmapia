package nsl.webmapia.game.domain.skill.exception;

public class UnsupportedSkillTypeException extends RuntimeException {

    public UnsupportedSkillTypeException(String message) {
        super(message);
    }

    public UnsupportedSkillTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedSkillTypeException(Throwable cause) {
        super(cause);
    }
}

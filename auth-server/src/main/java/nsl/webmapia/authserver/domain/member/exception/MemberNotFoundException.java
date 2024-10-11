package nsl.webmapia.authserver.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {
    private String memberId;

    public MemberNotFoundException(String memberId) {
        this.memberId = memberId;
    }

    public MemberNotFoundException(String message, String memberId) {
        super(message);
        this.memberId = memberId;
    }

    public MemberNotFoundException(String message, Throwable cause, String memberId) {
        super(message, cause);
        this.memberId = memberId;
    }

    public MemberNotFoundException(Throwable cause, String memberId) {
        super(cause);
        this.memberId = memberId;
    }
}

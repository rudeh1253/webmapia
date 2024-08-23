package nsl.webmapia.game.common.rest;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class BaseResponse {
    private final Integer statusCode;
    private final Integer statusCodeSeries;
    private final String message;
    private final Object content;

    public static BaseResponse ok() {
        return BaseResponse.ok("", null);
    }

    public static BaseResponse ok(String message) {
        return BaseResponse.ok(message, null);
    }

    public static BaseResponse ok(Object content) {
        return BaseResponse.ok("", content);
    }

    public static BaseResponse ok(String message, Object content) {
        return build(HttpStatus.OK, message, content);
    }

    public static BaseResponse created() {
        return BaseResponse.created("", null);
    }

    public static BaseResponse created(String message) {
        return BaseResponse.created(message, null);
    }

    public static BaseResponse created(Object content) {
        return BaseResponse.created("", content);
    }

    public static BaseResponse created(String message, Object content) {
        return build(HttpStatus.CREATED, message, content);
    }

    public static BaseResponse found() {
        return found("", null);
    }

    public static BaseResponse found(String message) {
        return found(message, null);
    }

    public static BaseResponse found(Object content) {
        return found("", content);
    }

    public static BaseResponse found(String message, Object content) {
        return build(HttpStatus.FOUND, message, content);
    }

    public static BaseResponse badRequest() {
        return badRequest("", null);
    }

    public static BaseResponse badRequest(String message) {
        return badRequest(message, null);
    }

    public static BaseResponse badRequest(Object content) {
        return badRequest("", content);
    }

    public static BaseResponse badRequest(String message, Object content) {
        return build(HttpStatus.BAD_REQUEST, message, content);
    }

    public static BaseResponse notFound() {
        return notFound("", null);
    }

    public static BaseResponse notFound(String message) {
        return notFound(message, null);
    }

    public static BaseResponse notFound(Object content) {
        return notFound("", content);
    }

    public static BaseResponse notFound(String message, Object content) {
        return build(HttpStatus.NOT_FOUND, message, content);
    }

    public static BaseResponse internalServerError() {
        return internalServerError("", null);
    }

    public static BaseResponse internalServerError(String message) {
        return internalServerError(message, null);
    }

    public static BaseResponse internalServerError(Object content) {
        return internalServerError("", content);
    }

    public static BaseResponse internalServerError(String message, Object content) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, content);
    }

    private static BaseResponse build(HttpStatus httpStatus, String message, Object content) {
        return BaseResponse.builder()
                .statusCode(httpStatus.value())
                .statusCodeSeries(httpStatus.series().value())
                .message(message)
                .content(content)
                .build();
    }
}

package nsl.webmapia.game.common.rest;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class BaseResponse<T> {
    private final Integer statusCode;
    private final Integer statusCodeSeries;
    private final String message;
    private final Object content;

    public static <T> BaseResponse<T> ok() {
        return BaseResponse.ok("", null);
    }

    public static <T> BaseResponse<T> ok(String message) {
        return BaseResponse.ok(message, null);
    }

    public static <T> BaseResponse<T> ok(T content) {
        return BaseResponse.ok("", content);
    }

    public static <T> BaseResponse<T> ok(String message, T content) {
        return build(HttpStatus.OK, message, content);
    }

    public static <T> BaseResponse<T> created() {
        return BaseResponse.created("", null);
    }

    public static <T> BaseResponse<T> created(String message) {
        return BaseResponse.created(message, null);
    }

    public static <T> BaseResponse<T> created(T content) {
        return BaseResponse.created("", content);
    }

    public static <T> BaseResponse<T> created(String message, T content) {
        return build(HttpStatus.CREATED, message, content);
    }

    public static <T> BaseResponse<T> found() {
        return found("", null);
    }

    public static <T> BaseResponse<T> found(String message) {
        return found(message, null);
    }

    public static <T> BaseResponse<T> found(T content) {
        return found("", content);
    }

    public static <T> BaseResponse<T> found(String message, T content) {
        return build(HttpStatus.FOUND, message, content);
    }

    public static <T> BaseResponse<T> badRequest() {
        return badRequest("", null);
    }

    public static <T> BaseResponse<T> badRequest(String message) {
        return badRequest(message, null);
    }

    public static <T> BaseResponse<T> badRequest(T content) {
        return badRequest("", content);
    }

    public static <T> BaseResponse<T> badRequest(String message, T content) {
        return build(HttpStatus.BAD_REQUEST, message, content);
    }

    public static <T> BaseResponse<T> notFound() {
        return notFound("", null);
    }

    public static <T> BaseResponse<T> notFound(String message) {
        return notFound(message, null);
    }

    public static <T> BaseResponse<T> notFound(T content) {
        return notFound("", content);
    }

    public static <T> BaseResponse<T> notFound(String message, T content) {
        return build(HttpStatus.NOT_FOUND, message, content);
    }

    public static <T> BaseResponse<T> internalServerError() {
        return internalServerError("", null);
    }

    public static <T> BaseResponse<T> internalServerError(String message) {
        return internalServerError(message, null);
    }

    public static <T> BaseResponse<T> internalServerError(T content) {
        return internalServerError("", content);
    }

    public static <T> BaseResponse<T> internalServerError(String message, T content) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, content);
    }

    private static <T> BaseResponse<T> build(HttpStatus httpStatus, String message, T content) {
        return BaseResponse.<T>builder()
                .statusCode(httpStatus.value())
                .statusCodeSeries(httpStatus.series().value())
                .message(message)
                .content(content)
                .build();
    }
}

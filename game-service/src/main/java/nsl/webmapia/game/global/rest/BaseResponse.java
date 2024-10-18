package nsl.webmapia.game.global.rest;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class BaseResponse<C> {
    private Integer statusCode;
    private Integer statusCodeSeries;
    private String message;
    private C content;

    public static <C> BaseResponse<C> ok() {
        return BaseResponse.ok("", null);
    }

    public static <C> BaseResponse<C> ok(String message) {
        return BaseResponse.ok(message, null);
    }

    public static <C> BaseResponse<C> ok(C content) {
        return BaseResponse.ok("", content);
    }

    public static <C> BaseResponse<C> ok(String message, C content) {
        return build(HttpStatus.OK, message, content);
    }

    public static <C> BaseResponse<C> created() {
        return BaseResponse.created("", null);
    }

    public static <C> BaseResponse<C> created(String message) {
        return BaseResponse.created(message, null);
    }

    public static <C> BaseResponse<C> created(C content) {
        return BaseResponse.created("", content);
    }

    public static <C> BaseResponse<C> created(String message, C content) {
        return build(HttpStatus.CREATED, message, content);
    }

    public static <C> BaseResponse<C> found() {
        return found("", null);
    }

    public static <C> BaseResponse<C> found(String message) {
        return found(message, null);
    }

    public static <C> BaseResponse<C> found(C content) {
        return found("", content);
    }

    public static <C> BaseResponse<C> found(String message, C content) {
        return build(HttpStatus.FOUND, message, content);
    }

    public static <C> BaseResponse <C> badRequest() {
        return badRequest("", null);
    }

    public static <C> BaseResponse<C> badRequest(String message) {
        return badRequest(message, null);
    }

    public static <C> BaseResponse<C> badRequest(C content) {
        return badRequest("", content);
    }

    public static <C> BaseResponse<C> badRequest(String message, C content) {
        return build(HttpStatus.BAD_REQUEST, message, content);
    }

    public static <C> BaseResponse<C> notFound() {
        return notFound("", null);
    }

    public static <C> BaseResponse<C> notFound(String message) {
        return notFound(message, null);
    }

    public static <C> BaseResponse<C> notFound(C content) {
        return notFound("", content);
    }

    public static <C> BaseResponse<C> notFound(String message, C content) {
        return build(HttpStatus.NOT_FOUND, message, content);
    }

    public static <C> BaseResponse<C> internalServerError() {
        return internalServerError("", null);
    }

    public static <C> BaseResponse<C> internalServerError(String message) {
        return internalServerError(message, null);
    }

    public static <C> BaseResponse<C> internalServerError(C content) {
        return internalServerError("", content);
    }

    public static <C> BaseResponse<C> internalServerError(String message, C content) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, content);
    }

    private static <C> BaseResponse<C> build(HttpStatus httpStatus, String message, C content) {
        return BaseResponse.<C>builder()
                .statusCode(httpStatus.value())
                .statusCodeSeries(httpStatus.series().value())
                .message(message)
                .content(content)
                .build();
    }
}

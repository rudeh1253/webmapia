package nsl.webmapia.game.global.rest;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int statusCode,
        int statusCodeSeries,
        String message,
        Object content,
        String errorCode,
        String errorName
) {
}

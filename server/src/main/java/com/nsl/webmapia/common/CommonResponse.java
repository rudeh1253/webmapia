package com.nsl.webmapia.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommonResponse {
    private int status;
    private String message;
    private LocalDateTime dateTime;
    private Object data;

    public CommonResponse(int status, String message, LocalDateTime dateTime, Object data) {
        this.status = status;
        this.dateTime = dateTime;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<CommonResponse> ok(Object data, LocalDateTime dateTime) {
        return new ResponseEntity<>(new CommonResponse(200, "Success", dateTime, data), HttpStatus.OK);
    }

    public static ResponseEntity<CommonResponse> created(Object data, LocalDateTime dateTime) {
        return new ResponseEntity<>(new CommonResponse(201, "Success", dateTime, data), HttpStatus.CREATED);
    }
}

package com.nsl.webmapia.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
public class CommonResponse {
    private int status;
    private String message;
    private Object data;

    public CommonResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<CommonResponse> ok(Object data) {
        return new ResponseEntity<>(new CommonResponse(200, "Success", data), HttpStatus.OK);
    }

    public static ResponseEntity<CommonResponse> created(Object data) {
        return new ResponseEntity<>(new CommonResponse(201, "Success", data), HttpStatus.CREATED);
    }
}

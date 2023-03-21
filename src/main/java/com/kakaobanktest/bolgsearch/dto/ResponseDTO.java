package com.kakaobanktest.bolgsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public class ResponseDTO {

    private int code;
    private HttpStatus status;
    private Boolean success;
    private String message;
    private Object data;

    public ResponseDTO(int code, HttpStatus status, Boolean success, String message) {
        this.code = code;
        this.status = status;
        this.success = success;
        this.message = message;
    }
}

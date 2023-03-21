package com.kakaobanktest.bolgsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public class ResponseDTO {
    private final HttpStatus code;
    private final Boolean success;
    private final String message;
    private final Object data;
}

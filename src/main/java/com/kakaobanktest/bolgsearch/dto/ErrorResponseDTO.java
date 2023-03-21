package com.kakaobanktest.bolgsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

// 쓰지말고 responseDTO와 합치기? @todo
@Getter
@ToString
@AllArgsConstructor
public class ErrorResponseDTO {
    private HttpStatus code;
    private String message;
}

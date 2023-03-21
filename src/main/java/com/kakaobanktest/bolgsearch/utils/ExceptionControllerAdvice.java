package com.kakaobanktest.bolgsearch.utils;

import com.kakaobanktest.bolgsearch.dto.ErrorResponseDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.kakaobanktest.blogsearch") // @todo blogsearch 하위 컨트롤러 전체
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDTO handleException(Exception ex) {
        return new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ErrorResponseDTO handleBadRequestException(Exception ex) {
        return new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ErrorResponseDTO handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return new ErrorResponseDTO(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}

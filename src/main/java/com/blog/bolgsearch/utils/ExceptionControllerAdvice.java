package com.blog.bolgsearch.utils;

import com.blog.bolgsearch.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice("com.kakaobanktest.bolgsearch") // @todo 삭제 blogsearch 하위 컨트롤러 전체
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseDTO handleException(Exception e) {
        log.error("[ExceptionHandler]\n", e);
        return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, false,  "서버 내부 오류가 발생하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseDTO handleBadRequestException(Exception e) {
        log.error("[IllegalArgumentExceptionHandler]\n", e);
        return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, false, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseDTO handleNotFoundException(ChangeSetPersister.NotFoundException e) {
        log.error("[NotFoundExceptionHandler]\n", e);
        return new ResponseDTO(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, false, e.getMessage());
    }

}

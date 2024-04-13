package com.fastcampus.boardserver.exception.handler;

import com.fastcampus.boardserver.dto.response.CommonResponse;
import com.fastcampus.boardserver.exception.BoardServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeExceptionException(RuntimeException runtimeException) {
        CommonResponse commonResponse = new CommonResponse(
                HttpStatus.OK,
                "RuntimeException",
                runtimeException.getMessage(),
                runtimeException.getMessage()
        );

        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

    @ExceptionHandler({BoardServerException.class})
    public ResponseEntity<Object> handleBoardServerException(BoardServerException boardServerException) {
        CommonResponse commonResponse = new CommonResponse(
                HttpStatus.OK,
                "BoardServerException",
                boardServerException.getMessage(),
                boardServerException.getMessage()
        );

        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> defaultException(Exception exception) {
        CommonResponse commonResponse = new CommonResponse(
                HttpStatus.OK,
                "BoardServerException",
                exception.getMessage(),
                exception.getMessage()
        );

        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }
}

package com.fastcampus.boardserver.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class BoardServerException extends RuntimeException {
    HttpStatus code;
    String msg;
}
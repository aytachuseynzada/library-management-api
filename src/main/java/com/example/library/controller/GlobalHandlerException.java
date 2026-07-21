package com.example.library.controller;

import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.MemberNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalHandlerException {
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(AuthorNotFoundException.class)
    public ErrorResponse handleException(AuthorNotFoundException ex){
        return new ErrorResponse("author.not.found", ex.getMessage());
    }
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(BookNotFoundException.class)
    public ErrorResponse handleException(BookNotFoundException ex){
        return new ErrorResponse("book.not.found", ex.getMessage());
    }
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponse handleException(MemberNotFoundException ex){
        return new ErrorResponse("member.not.found", ex.getMessage());
    }
}

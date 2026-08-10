package com.example.library.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

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
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleException(MethodArgumentNotValidException ex) {
        return new ErrorResponse("validation.failed", ex.getBindingResult().getFieldError().getDefaultMessage());
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleException(IllegalArgumentException ex) {
        return new ErrorResponse("invalid.argument", ex.getMessage());
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleException(BadCredentialsException ex) {
        return new ErrorResponse("invalid.credentials", "Username or password is incorrect");
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ErrorResponse handleException(UsernameAlreadyExistsException ex) {
        return new ErrorResponse("username.already.exists", ex.getMessage());
    }
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(LoanNotFoundException.class)
    public ErrorResponse handleException(LoanNotFoundException ex){
        return new ErrorResponse("loan.not.found", ex.getMessage());
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse handleException(IllegalStateException ex) {
        return new ErrorResponse("invalid.state", ex.getMessage());
    }
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    @ExceptionHandler(FileTooLargeException.class)
    public ErrorResponse handleException(FileTooLargeException ex) {
        return new ErrorResponse("file.too.large", ex.getMessage());
    }
}

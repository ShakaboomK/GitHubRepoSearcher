package com.KKStands.GitHubExtends.GitHubRepoKK.Utils;

import com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions.GHubBaseAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GHubGlobalExceptionHandler {
    @ExceptionHandler(GHubBaseAppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(GHubBaseAppException ex) {
        ErrorResponse error = new ErrorResponse(ex.getStatus(), ex.getMessage());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatus()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.error(errorMessage);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred");
        ex.printStackTrace(); // Optional logging
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
class ErrorResponse {
    private final int status;
    private final String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
}

package com.bt2.spotify_consumer.config;

import com.bt2.spotify_consumer.dto.ResponseError;
import com.bt2.spotify_consumer.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseError> handleUnauthorizedException(UnauthorizedException ex) {
        ResponseError responseError = new ResponseError(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseError);
    }

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<ResponseError> handleTooManyRequestsException(HttpClientErrorException.TooManyRequests ex){
        ResponseError responseError = new ResponseError(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too many requests. Try again soon.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(responseError);
    }

    // Generic handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGeneralException(Exception ex) {
        ResponseError responseError = new ResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                "An unexpected error occurred"
        );
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }
}
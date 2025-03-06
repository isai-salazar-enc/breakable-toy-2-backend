package com.bt2.spotify_consumer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ResponseError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ResponseError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}

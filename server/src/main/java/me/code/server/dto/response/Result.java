package me.code.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
public abstract class Result<T> implements ResponseEntityConvertible<Result<T>> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    protected LocalDateTime timeStamp;

    @JsonProperty("isSuccessful")
    protected Boolean isSuccessful;

    @JsonProperty("status")
    protected HttpStatus status;

    @JsonProperty("message")
    protected String message;

    public Result(HttpStatus status, String message, boolean isSuccessful) {
        this.timeStamp = LocalDateTime.now();
        this.isSuccessful = isSuccessful;
        this.status = status;
        this.message = message;
    }

    @Override
    public ResponseEntity<Result<T>> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }

}
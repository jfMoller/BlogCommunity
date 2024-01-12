package me.code.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public abstract class Result {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    protected LocalDateTime timeStamp;

    @JsonProperty("success")
    protected Boolean success;

    @JsonProperty("error")
    protected Boolean error;

    @JsonProperty("status")
    protected HttpStatus status;

    @JsonProperty("message")
    protected String message;

    public Result(HttpStatus status, String message, boolean isSuccess) {
        this.timeStamp = LocalDateTime.now();
        this.success = isSuccess;
        this.error = !isSuccess;
        this.status = status;
        this.message = message;
    }

}
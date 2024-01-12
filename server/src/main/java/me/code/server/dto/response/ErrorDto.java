package me.code.server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorDto extends Result {

    @JsonProperty("details")
    private String exceptionMessage;

    public ErrorDto(HttpStatus status, String message, String exceptionMessage) {
        super(status, message, false);
        this.exceptionMessage = exceptionMessage;
    }

    public ResponseEntity<ErrorDto> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}

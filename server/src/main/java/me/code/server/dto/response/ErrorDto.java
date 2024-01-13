package me.code.server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorDto extends Result implements ResponseEntityConvertible<ErrorDto> {

    @JsonProperty("errorDetails")
    private String errorDetails;

    public ErrorDto(HttpStatus status, String message, String errorDetails) {
        super(status, message, false);
        this.errorDetails = errorDetails;
    }

    @Override
    public ResponseEntity<ErrorDto> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }
}

package me.code.server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class ErrorDto extends Result<ErrorDto> {

    @JsonProperty("errorDetails")
    private String errorDetails;

    public ErrorDto(HttpStatus status, String message, String errorDetails) {
        super(status, message, false);
        this.errorDetails = errorDetails;
    }

}
